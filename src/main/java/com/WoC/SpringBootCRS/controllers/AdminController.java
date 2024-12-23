package com.WoC.SpringBootCRS.controllers;

import com.WoC.SpringBootCRS.dtos.*;
import com.WoC.SpringBootCRS.services.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/add-student")
    public ResponseEntity<?> addStudent(@Valid @RequestBody StudentDto studentDto, BindingResult result) {
        if(result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());

            return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
        }
        try{
            adminService.addStudentByAdmin(studentDto);
            return ResponseEntity.ok("Student added successfully");
        }catch (Exception e){
            System.out.println("ERROR: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error adding student: " + e.getMessage());
        }
    }

    @PostMapping("/add-professor")
    public ResponseEntity<?> addProfessor(@RequestBody ProfessorDto professorDto,BindingResult result) {
        if(result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());

            return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
        }
        try {
            adminService.addProfessorByAdmin(professorDto);
            return ResponseEntity.ok("Professor added successfully");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error adding professor: " + e.getMessage());
        }
    }

    @PostMapping("/add-semester")
    public ResponseEntity<?> addSemester(@RequestBody SemesterDto semesterDto,BindingResult result) {
        if(result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());

            return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
        }
        try {
            adminService.addSemesterByAdmin(semesterDto);
            return ResponseEntity.ok("Semester added successfully");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error adding semester: " + e.getMessage());
        }
    }

    @PostMapping("/add-course")
    public ResponseEntity<?> addCourse(@RequestBody CourseDto courseDto,BindingResult result) {
        if(result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());

            return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
        }
        try {
            adminService.addCourseByAdmin(courseDto);
            return ResponseEntity.ok("Course added successfully");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error adding course: " + e.getMessage());
        }
    }

    @GetMapping("/assign-course")
    public ResponseEntity<?> assignCourse() {
        try {
            adminService.assignCourse();
            return ResponseEntity.ok("Course assigned successfully");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error assigning course: " + e.getMessage());
        }
    }

    @GetMapping("/send-course-assign-mail")
    public ResponseEntity<?> sendCourseAssignMail() {
        try {
            adminService.sendEmailNotificationOfAssignCorseList();
            return ResponseEntity.ok("Email Send successfully");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error sending course-assign mail: " + e.getMessage());
        }
    }

    @GetMapping("/send-mail-notification")
    public ResponseEntity<?> sendMailNotification() {
        try {
            adminService.sendEmailNotificationFormDeadline();
            return ResponseEntity.ok("Email notification sent successfully");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error sending email notification: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete-course/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        try {
            adminService.deleteCourseByAdmin(id);
            return ResponseEntity.ok("Course deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error deleting course: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete-student/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        try {
            adminService.deleteStudentByAdmin(id);
            return ResponseEntity.ok("Student deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error deleting student: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete-professor/{id}")
    public ResponseEntity<?> deleteProfessor(@PathVariable Long id) {
        try {
            adminService.deleteProfessorByAdmin(id);
            return ResponseEntity.ok("Professor deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error deleting professor: " + e.getMessage());
        }
    }

    @PutMapping("/update-course/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @RequestBody CourseDto courseDto, BindingResult result) {
        if(result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());

            return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
        }
        try {
            adminService.updateCourseByAdmin(id, courseDto);
            return ResponseEntity.ok("Course updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating course: " + e.getMessage());
        }
    }

    @PutMapping("/update-semester/{id}")
    public ResponseEntity<?> updateSemester(@PathVariable Long id, @RequestBody SemesterDto semesterDto,BindingResult result){
        if(result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());

            return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
        }
        try {
            adminService.updateSemesterByAdmin(id, semesterDto);
            return ResponseEntity.ok("Semester updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating course: " + e.getMessage());
        }
    }


}
