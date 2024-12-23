package com.WoC.SpringBootCRS.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;


import java.util.List;
import java.util.Set;

@Entity
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String courseName;
    private int capacity;
    private int remainingSeats;
    private int courseCredit;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "professor_id",nullable = false)
    @ToString.Exclude
    @JsonBackReference
    private Professor professor;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "semester_id", nullable = false)
    @ToString.Exclude
    private Semester semester;

    @ManyToMany(mappedBy = "courses",cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Student> students;

    @ManyToMany(mappedBy = "requestedCourses",cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<CourseRequestForm> courseRequestForms;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public void setCourseRequestForms(List<CourseRequestForm> courseRequestForms) {
        this.courseRequestForms = courseRequestForms;
    }

    public int getCourseCredit() {
        return courseCredit;
    }

    public void setCourseCredit(int courseCredit) {
        this.courseCredit = courseCredit;
    }

    public List<Student> getStudents() {
        return students;
    }

    public List<CourseRequestForm> getCourseRequestForms() {
        return courseRequestForms;
    }
}
