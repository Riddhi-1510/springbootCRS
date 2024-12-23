package com.WoC.SpringBootCRS.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class CourseRequestForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @ToString.Exclude
    @JoinTable(
            name = "course_request_courses",
            joinColumns = @JoinColumn(name = "course_request_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private List<Course> requestedCourses;

    private LocalDateTime requestedDate;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @ToString.Exclude
    private Student student;

    public void setRequestedCourses(List<Course> requestedCourses) {
        this.requestedCourses = requestedCourses;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(LocalDateTime requestedDate) {
        this.requestedDate = requestedDate;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
