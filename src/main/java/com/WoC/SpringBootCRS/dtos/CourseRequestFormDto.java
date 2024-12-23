package com.WoC.SpringBootCRS.dtos;

import com.WoC.SpringBootCRS.entities.Course;
import com.WoC.SpringBootCRS.entities.Student;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class CourseRequestFormDto {

    @NotBlank(message = "Requested course IDs cannot be empty")
    private List<Long> requestedCourseIds;
    private LocalDateTime requestedDate;
    @NotNull(message = "Student ID cannot be null")
    private Long studentId;



    public LocalDateTime getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(LocalDateTime requestedDate) {
        this.requestedDate = requestedDate;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public void setRequestedCourseIds(List<Long> requestedCourseIds) {
        this.requestedCourseIds = requestedCourseIds;
    }
}
