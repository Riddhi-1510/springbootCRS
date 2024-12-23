package com.WoC.SpringBootCRS.services.implementation;

import com.WoC.SpringBootCRS.dtos.*;
import com.WoC.SpringBootCRS.entities.*;
import com.WoC.SpringBootCRS.repositories.*;
import com.WoC.SpringBootCRS.services.AdminService;
import com.WoC.SpringBootCRS.services.ProfessorService;
import com.WoC.SpringBootCRS.util.EmailService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

@Service
public class AdminServiceImp implements AdminService {


    @Autowired
    ModelMapper modelMapper;
    @Autowired
    StudentRepo studentRepo;
    @Autowired
    CourseRepo courseRepo;
    @Autowired
    ProfessorRepo profRepo;
    @Autowired
    SemesterRepo semRepo;
    @Autowired
    private SemesterRepo semesterRepo;
    @Autowired
    private EmailService emailService;
    @Autowired
    private CourseRequestFormRepo courseRequestFormRepo;
    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ProfessorRepo professorRepo;

    @Override
    @Transactional
    public void addAdmin(AdminDto adminDto) {

        String encodep = passwordEncoder.encode(adminDto.getPassword());
        adminDto.setPassword(encodep);
        Admin admin = modelMapper.map(adminDto, Admin.class);
        adminRepo.save(admin);
    }

    @Override
    @Transactional
    public void sendEmailNotificationFormDeadline() {
        List<String> studentEmails = studentRepo.findAllEmails();
        for (String studentEmail : studentEmails) {
            emailService.sendSimpleMail(studentEmail,"Course-Registration-TimeLine","Dear Student,\n\nThis is a reminder that the deadline is on Tomorrow: 4PM"+
                    ".\n\nPlease make sure to submit your Form before the deadline.\n\nBest regards,\nDA-IICT Admin");
        }
    }

    @Override
    @Transactional
    public void addCourseByAdmin(CourseDto courseDto){
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Professor professor = profRepo.findById(courseDto.getProfessorId())
                .orElseThrow(() -> new RuntimeException("Professor Not Found"));

        Semester semester = semesterRepo.findById(courseDto.getSemesterId()).orElseThrow(() -> new RuntimeException("Semester Not Found"));
        Course course = modelMapper.map(courseDto, Course.class);
        course.setProfessor(professor);
        course.setSemester(semester);
        courseRepo.save(course);
    }

    @Override
    @Transactional
    public void addStudentByAdmin(StudentDto studentDto) {
        try {
            String encodep = passwordEncoder.encode(studentDto.getPassword());
            studentDto.setPassword(encodep);
            Student student = modelMapper.map(studentDto, Student.class);
            //System.out.println("Student: " + student);

            Student savedStudent = studentRepo.save(student);
            System.out.println("Saved Student: " + savedStudent);

        } catch (Exception e) {
            System.err.println("Error adding student: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void addProfessorByAdmin(ProfessorDto professorDto) {

        String encodep = passwordEncoder.encode(professorDto.getPassword());
        professorDto.setPassword(encodep);

        Professor professor = modelMapper.map(professorDto, Professor.class);
        //System.out.println("Professor IS: " + professor);
        profRepo.save(professor);
    }

    @Override
    @Transactional
    public void addSemesterByAdmin(SemesterDto semesterDto) {
        Semester semester = modelMapper.map(semesterDto, Semester.class);
        //System.out.println("Semester IS: " + semester);
        semRepo.save(semester);
    }

    @Override
    @Transactional
    public void assignCourse() {

        List<CourseRequestForm> courseRequests = courseRequestFormRepo.findAll(Sort.by("requestedDate"));

        for(CourseRequestForm request : courseRequests){
//            System.out.println(request.getRequestedCourses());
              List<Course> allocatedCourses = new ArrayList<>();
              for(Course course : request.getRequestedCourses()){
                  Long courseId = course.getId();
                  if(course.getRemainingSeats()!=0){
                      allocatedCourses.add(course);
                      Student student = request.getStudent();
                      student.getCourses().add(course);
                      course.getStudents().add(student);
                      course.setRemainingSeats(course.getRemainingSeats()-1);
                      studentRepo.save(student);
                      courseRepo.save(course);
                  }
                  if(allocatedCourses.size()==3){
                      break;
                  }
              }
        }
//  Course Allocation Done
//        for (CourseRequestForm request : courseRequests) {
//            Student student = request.getStudent();
//            System.out.println("Student ID: " + student.getId() + ", Allocated Courses: " + student.getCourses());
//        }

    }

    @Override
    @Transactional
    public void sendEmailNotificationOfAssignCorseList() {
        List<Student> students = studentRepo.findAll();
        for (Student student : students){
            String studentEmail = student.getEmail();
            List<Course> allocatedcourse = student.getCourses();

            List<String> courseName =allocatedcourse.stream().map(Course::getCourseName).collect(Collectors.toList());

            StringBuilder courseNames = new StringBuilder();
            for (String cname : courseName) {
                courseNames.append(cname).append("\n");
            }

            String emailContent = "Dear " + student.getName() + ",\n\n" +
                    "Congratulations! You have been successfully allocated the following courses:\n\n" +
                    courseNames.toString() +
                    "\nPlease check your portal for further details.\n\n" +
                    "Best regards,\nDA-IICT Admin";

            emailService.sendSimpleMail(studentEmail, "Course Allocation Confirmation", emailContent);

        }
    }

    @Override
    @Transactional
    public void deleteCourseByAdmin(Long id) {
        Course course = courseRepo.findById(id).orElseThrow(() -> new RuntimeException("Course Not Found"));

        if (course.getProfessor() != null) {
            course.getProfessor().getCourses().remove(course);
            course.setProfessor(null);
        }

        for (CourseRequestForm courseRequestForm : course.getCourseRequestForms()) {
            courseRequestForm.getRequestedCourses().remove(course);
            courseRequestFormRepo.save(courseRequestForm);
        }
        course.getCourseRequestForms().clear();

        for (Student student : course.getStudents()) {
            student.getCourses().remove(course);
            studentRepo.save(student);
        }
        course.getStudents().clear();

        Semester semester = course.getSemester();
        semester.getCourses().remove(course);
        semRepo.save(semester);

        courseRepo.delete(course);
    }

    @Override
    @Transactional
    public void deleteStudentByAdmin(Long id) {
        Student student = studentRepo.findById(id).orElseThrow(() -> new RuntimeException("Student Not Found"));

        for(Course course : student.getCourses()){
            course.getStudents().remove(student);
            course.setRemainingSeats(course.getRemainingSeats() + 1);
            courseRepo.save(course);
        }

        student.getCourses().clear();


        if (student.getCourseRequestForms() != null) {
            CourseRequestForm courseRequestForm = student.getCourseRequestForms();
            courseRequestForm.getRequestedCourses().clear();
            courseRequestFormRepo.delete(courseRequestForm);
        }

        //delete student
        studentRepo.delete(student);

        }

    @Override
    @Transactional
    public void deleteProfessorByAdmin(Long id) {
         Professor professor = profRepo.findById(id).orElseThrow(() -> new RuntimeException("Professor Not Found"));

         List<Course> courseList = new ArrayList<>(professor.getCourses());

         for (Course course : courseList) {
             deleteCourseByAdmin(course.getId());
         }

         professor.getCourses().clear();

         // Delete the professor
        profRepo.delete(professor);
    }

    @Override
    @Transactional
    public void updateCourseByAdmin(Long id, CourseDto courseDto) {

        Course oldcourse = courseRepo.findById(id).orElseThrow(()->new RuntimeException("Course Not Found"));

        if(courseDto.getCourseName()!=null)
            oldcourse.setCourseName(courseDto.getCourseName());
        if(courseDto.getCapacity()!=0)
            oldcourse.setCapacity(courseDto.getCapacity());
        if(courseDto.getCourseCredit()!=0)
            oldcourse.setCourseCredit(courseDto.getCourseCredit());

        if(courseDto.getProfessorId()!=null){
            Professor professor = profRepo.findById(courseDto.getProfessorId()).orElseThrow(()->new RuntimeException("Professor Not Found"));
            oldcourse.setProfessor(professor);
        }

        if(courseDto.getSemesterId()!=null){
            Semester semester = semesterRepo.findById(courseDto.getSemesterId()).orElseThrow(()->new RuntimeException("Semester Not Found"));
            oldcourse.setSemester(semester);
        }
        //update done
        courseRepo.save(oldcourse);
    }

    @Override
    @Transactional
    public void updateSemesterByAdmin(Long id, SemesterDto semesterDto) {
        Semester oldSemester = semesterRepo.findById(id).orElseThrow(()->new RuntimeException("Semester Not Found"));

        if(semesterDto.getSemesterName()!=null)
            oldSemester.setSemesterName(semesterDto.getSemesterName());
        if(semesterDto.getEndDate()!=null)
            oldSemester.setEndDate(semesterDto.getEndDate());
        if(semesterDto.getStartDate()!=null)
            oldSemester.setStartDate(semesterDto.getStartDate());

        //update semester
        semesterRepo.save(oldSemester);
    }


}
