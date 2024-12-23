package com.WoC.SpringBootCRS.dtos;

import com.WoC.SpringBootCRS.entities.Course;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDate;


@Data
public class SemesterDto {
    @NotBlank(message = "Semester name cannot be empty")
    private String semesterName;

    @NotNull(message = "Start date cannot be null")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDate;

    public String getSemesterName() {
        return semesterName;
    }

    public void setSemesterName(String semesterName) {
        this.semesterName = semesterName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
