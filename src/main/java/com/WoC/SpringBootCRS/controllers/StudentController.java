package com.WoC.SpringBootCRS.controllers;

import com.WoC.SpringBootCRS.dtos.CourseRequestFormDto;
import com.WoC.SpringBootCRS.dtos.StudentDto;
import com.WoC.SpringBootCRS.services.StudentService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/get-course-list")
    public ResponseEntity<?> getCourseList() {
        try {
            List<Map<String,String>> coursesList = studentService.getAllCourseList();
            return ResponseEntity.ok(coursesList);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error For Getting CourseList by Student." + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudent(@PathVariable Long id,@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        try {
            Map<String, String> mp = studentService.getStudentDetailsById(id,token);
            return ResponseEntity.ok(mp);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error For Getting Student Details:" + e.getMessage());
        }
    }

    @PostMapping("/submit-course-form")
    public ResponseEntity<?> submitCourseForm(@RequestBody CourseRequestFormDto courseRequestFormDto,@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        try{
            studentService.submitCourseRequestForm(courseRequestFormDto,token);
            return ResponseEntity.ok("CourseRequest Form submitted successfully");
        }catch(Exception e){
            //System.out.println(e.getStackTrace());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error adding CourseRequest Form: " + e);
        }
    }

    @PutMapping("/update-student/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @RequestBody StudentDto studentDto, @RequestHeader(HttpHeaders.AUTHORIZATION) String token, BindingResult result) {
        if(result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());

            return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
        }
        try {
            studentService.updateStudent(id, studentDto,token);
            return ResponseEntity.ok("Student updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating student: " + e.getMessage());
        }
    }

}
