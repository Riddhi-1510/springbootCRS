package com.WoC.SpringBootCRS.dtos;

import com.WoC.SpringBootCRS.entities.CourseRequestForm;
import com.WoC.SpringBootCRS.entities.Professor;
import com.WoC.SpringBootCRS.entities.Semester;
import com.WoC.SpringBootCRS.entities.Student;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class CourseDto {

    @NotBlank(message = "course name can not be empty")
    private String courseName;

    @Min(value = 1, message = "capacity must be greater than zero")
    private int capacity;

    @Min(value = 0, message = "remaining seats cannot be negative")
    private int remainingSeats;

    @NotNull(message = "professor ID cannot be null")
    private Long professorId;

    @NotNull(message = "semester ID cannot be null")
    private Long semesterId;

    @Min(value = 1, message = "course credit must be greater than zero")
    private int courseCredit;


    public int getCourseCredit() {
        return courseCredit;
    }

    public void setCourseCredit(int courseCredit) {
        this.courseCredit = courseCredit;
    }


    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getRemainingSeats() {
        return remainingSeats;
    }

    public void setRemainingSeats(int remainingSeats) {
        this.remainingSeats = remainingSeats;
    }

    public Long getProfessorId() {
        return professorId;
    }

    public void setProfessorId(Long professorId) {
        this.professorId = professorId;
    }

    public Long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Long semesterId) {
        this.semesterId = semesterId;
    }
}
