package com.WoC.SpringBootCRS.services;


import com.WoC.SpringBootCRS.dtos.CourseDto;
import com.WoC.SpringBootCRS.dtos.ProfessorDto;
import com.WoC.SpringBootCRS.entities.Professor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface ProfessorService {

    List<Map<String,String>> getAllCourseByProfessorId(Long pid,String token);
    Map<String,String> getProfessorById(Long pid,String token);
    void updateProfessor(Long id, ProfessorDto professorDto, String token);

}
