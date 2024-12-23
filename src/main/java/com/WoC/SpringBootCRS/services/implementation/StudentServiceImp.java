package com.WoC.SpringBootCRS.services.implementation;

import com.WoC.SpringBootCRS.dtos.CourseRequestFormDto;
import com.WoC.SpringBootCRS.dtos.StudentDto;
import com.WoC.SpringBootCRS.entities.Course;
import com.WoC.SpringBootCRS.entities.CourseRequestForm;
import com.WoC.SpringBootCRS.entities.Student;
import com.WoC.SpringBootCRS.repositories.CourseRepo;
import com.WoC.SpringBootCRS.repositories.CourseRequestFormRepo;
import com.WoC.SpringBootCRS.repositories.ProfessorRepo;
import com.WoC.SpringBootCRS.repositories.StudentRepo;
import com.WoC.SpringBootCRS.services.StudentService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class StudentServiceImp implements StudentService {

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    StudentRepo studentRepo;
    @Autowired
    CourseRepo courseRepo;
    @Autowired
    ProfessorRepo profRepo;
    @Autowired
    CourseRequestFormRepo courseRequestFormRepo;
    @Value("${security.jwt.secret-key}")
    private String secret;


    @Override
    public List<Map<String, String>> getAllCourseList() {
        List<Map<String,String>> courseNames = new ArrayList<>();
        List<Course> clist =  courseRepo.findAll();
        for (Course course : clist) {
            Map<String,String> courseMap = new HashMap<>();
            courseMap.put("CourseName",course.getCourseName());
            courseMap.put("CourseGrade",String.valueOf(course.getCourseCredit()));
            courseMap.put("CourseProf.",course.getProfessor().getName());
            courseMap.put("Course ID",String.valueOf(course.getId()));
            courseNames.add(courseMap);
        }

        if(courseNames == null) {
            throw new EntityNotFoundException("Error During GetAllCourseList");
        }

        return courseNames;
    }

    @Override
    public Map<String, String> getStudentDetailsById(Long sid, String token) {

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        String loggedInEmail = claims.getSubject();

        Student student = studentRepo.findById(sid).get();

        if (!loggedInEmail.equals(student.getEmail())) {
            throw new SecurityException("You are not authorized.");
        }

         Map<String, String> mp = new HashMap<>();
         if(student==null) {
             throw new EntityNotFoundException("Error During GetStudentDetailsById");
         }
         mp.put("Name",student.getName());
         mp.put("Email",student.getEmail());
         mp.put("Student Id",student.getId().toString());

        List<Course> courses = student.getCourses();
        List<String> courseNames = new ArrayList<>();
        for (Course course : courses) {
            courseNames.add(course.getCourseName());
        }
        if(courses==null || courses.size()==0) {
            mp.put("Message", "Student has not been assigned any courses yet.");
            return mp;
        }
        mp.put("Student Assign Courses: ",courseNames.toString());
        return mp;

    }

    @Override
    @Transactional
    public void submitCourseRequestForm(CourseRequestFormDto courseRequestFormDto, String token) {

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        String loggedInEmail = claims.getSubject();

        Student student = studentRepo.findById(courseRequestFormDto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student with ID " + courseRequestFormDto.getStudentId() + " not found"));

        if (!loggedInEmail.equals(student.getEmail())) {
            throw new SecurityException("You are not authorized.");
        }

        CourseRequestForm courseRequestForm = new CourseRequestForm();
        courseRequestForm.setStudent(student);

        List<Course> requestedCourses = new ArrayList<>();

        List<Long> requestedCourseIds = new ArrayList<>(courseRequestFormDto.getRequestedCourseIds());

        for (Long courseId : requestedCourseIds) {
            Course course = courseRepo.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course with ID " + courseId + " not found"));
            requestedCourses.add(course);
            //System.out.println("Course IN loop is: "+course);
        }

        courseRequestForm.setRequestedCourses(requestedCourses);
        courseRequestForm.setRequestedDate(LocalDateTime.now());
        courseRequestFormRepo.save(courseRequestForm);
    }

    @Override
    @Transactional
    public void updateStudent(Long id, StudentDto studentDto, String token) {

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        String loggedInEmail = claims.getSubject();


        Student oldStudent = studentRepo.findById(id).orElseThrow(()->new RuntimeException("Student Not Found"));

        if(!loggedInEmail.equals(oldStudent.getEmail())){
            throw new SecurityException("You are not authorized.");
        }

        if(studentDto.getEmail()!=null) oldStudent.setEmail(studentDto.getEmail());
        if(studentDto.getName()!=null)  oldStudent.setName(studentDto.getName());

        //update student
        studentRepo.save(oldStudent);

    }

}
