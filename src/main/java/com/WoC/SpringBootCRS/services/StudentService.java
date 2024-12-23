package com.WoC.SpringBootCRS.services;

import com.WoC.SpringBootCRS.dtos.CourseRequestFormDto;
import com.WoC.SpringBootCRS.dtos.StudentDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface StudentService {

    List<Map<String,String>> getAllCourseList();
    Map<String,String> getStudentDetailsById(Long sid, String token);
    void submitCourseRequestForm(CourseRequestFormDto courseRequestFormDto,String token);
    void updateStudent(Long id, StudentDto studentDto, String token);

}
