package com.WoC.SpringBootCRS.controllers;

import com.WoC.SpringBootCRS.dtos.CourseDto;
import com.WoC.SpringBootCRS.dtos.ProfessorDto;
import com.WoC.SpringBootCRS.entities.Professor;
import com.WoC.SpringBootCRS.services.ProfessorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/professor")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseByPid(@PathVariable Long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        try{
            List<Map<String,String>> coursesList = professorService.getAllCourseByProfessorId(id,token);
            return ResponseEntity.ok(coursesList);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error For Getting List of Course by Professor Id:  " + e.getMessage());
        }
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<?> getProfessorByPid(@PathVariable Long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        try {
            Map<String,String> pr = professorService.getProfessorById(id,token);
//            System.out.println("PROFESSOR : " + pr);
            return ResponseEntity.ok(pr);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error For Getting Professor Details by Professor Id:  " + e.getMessage());
        }
    }

    @PutMapping("/update-professor/{id}")
    public ResponseEntity<?> updateProfessor(@PathVariable Long id, @RequestBody ProfessorDto professorDto, @RequestHeader(HttpHeaders.AUTHORIZATION) String token, BindingResult result) {
        if(result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());

            return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
        }
        try {
            professorService.updateProfessor(id, professorDto,token);
            return ResponseEntity.ok("Professor updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating professor: " + e.getMessage());
        }
    }

}
