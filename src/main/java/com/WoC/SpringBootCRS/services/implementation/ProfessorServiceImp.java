package com.WoC.SpringBootCRS.services.implementation;

import com.WoC.SpringBootCRS.dtos.CourseDto;
import com.WoC.SpringBootCRS.dtos.ProfessorDto;
import com.WoC.SpringBootCRS.entities.Course;
import com.WoC.SpringBootCRS.entities.Professor;
import com.WoC.SpringBootCRS.repositories.*;
import com.WoC.SpringBootCRS.services.ProfessorService;
import com.WoC.SpringBootCRS.util.EmailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProfessorServiceImp implements ProfessorService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    StudentRepo studentRepo;
    @Autowired
    CourseRepo courseRepo;
    @Autowired
    ProfessorRepo profRepo;
    @Value("${security.jwt.secret-key}")
    private String secret;


    @Override
    public List<Map<String,String>> getAllCourseByProfessorId(Long pid, String token) {
        if (pid == null) {
            throw new IllegalArgumentException("Professor ID cannot be null");
        }

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        String loggedInEmail = claims.getSubject();


        Professor professor = profRepo.findById(pid).get();
        if(professor == null) {
            throw new IllegalArgumentException("Professor ID Can Be Wrong or Not Found");
        }

        if (!loggedInEmail.equals(professor.getEmail())) {
            throw new SecurityException("You are not authorized.");
        }


        List<Course> courseList = courseRepo.findByProfessorId(pid);

        List<Map<String,String>> courseNames = new ArrayList<>();
        for (Course course : courseList) {
            Map<String,String> courseMap = new HashMap<>();
            courseMap.put("CourseName: ",course.getCourseName());
            courseMap.put("CourseGrade: ",String.valueOf(course.getCourseCredit()));
            courseNames.add(courseMap);
        }

        if(courseNames == null) {
            throw new EntityNotFoundException("No courses found for Professor ID " + pid);
        }

        return courseNames;

    }

    @Override
    public Map<String,String> getProfessorById(Long pid, String token) {

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        String loggedInEmail = claims.getSubject();


        Professor pr= profRepo.findById(pid)
                .orElseThrow(() -> new EntityNotFoundException("Professor with ID " + pid + " not found"));

        if (!loggedInEmail.equals(pr.getEmail())) {
            throw new SecurityException("You are not authorized.");
        }

        Map<String,String> mp = new HashMap<>();
        mp.put("Name: ",pr.getName());
        mp.put("Email: ",pr.getEmail());
        mp.put("Professor ID: ",pr.getId().toString());
        List<Course> courseList = courseRepo.findByProfessorId(pid);
        if (courseList == null || courseList.isEmpty()) {
            throw new EntityNotFoundException("No courses found for Professor ID " + pid);
        }
        List<String> courseNames = courseList.stream().map(course -> course.getCourseName()).collect(Collectors.toList());
        mp.put("Courses: ",courseNames.toString());
        return mp;
    }

    @Override
    @Transactional
    public void updateProfessor(Long id, ProfessorDto professorDto, String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        String loggedInEmail = claims.getSubject();


        Professor oldProfessor = profRepo.findById(id).orElseThrow(()->new RuntimeException("Professor Not Found"));

        if (!loggedInEmail.equals(oldProfessor.getEmail())) {
            throw new SecurityException("You are not authorized.");
        }

        if(professorDto.getEmail()!=null) oldProfessor.setEmail(professorDto.getEmail());
        if(professorDto.getName()!= null) oldProfessor.setName(professorDto.getName());

        //update professor
        profRepo.save(oldProfessor);
    }

}
