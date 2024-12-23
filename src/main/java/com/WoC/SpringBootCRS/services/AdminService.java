package com.WoC.SpringBootCRS.services;

import com.WoC.SpringBootCRS.dtos.*;
import org.springframework.stereotype.Component;

@Component
public interface AdminService {

    void addAdmin(AdminDto adminDto);
    void sendEmailNotificationFormDeadline();
    void addCourseByAdmin(CourseDto courseDto);
    void addStudentByAdmin(StudentDto studentDto);
    void addProfessorByAdmin(ProfessorDto professorDto);
    void addSemesterByAdmin(SemesterDto semesterDto);
    void assignCourse();
    void sendEmailNotificationOfAssignCorseList();
    void deleteCourseByAdmin(Long id);
    void deleteStudentByAdmin(Long id);
    void deleteProfessorByAdmin(Long id);
    void updateCourseByAdmin(Long id, CourseDto courseDto);
    void updateSemesterByAdmin(Long id, SemesterDto semesterDto);
}
