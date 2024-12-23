package com.WoC.SpringBootCRS.dtos;

import com.WoC.SpringBootCRS.entities.Course;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class ProfessorDto {

    @NotBlank(message = "Name can not be Empty")
    @Size(min = 3, max = 15,message = "Name Minimum size is 3 & Maximum size is 10")
    private String name;

    @Email(message = "Invalid Email Format")
    @NotBlank(message = "Email can not be Empty")
    private String email;

    @NotBlank(message = "password can not be Empty")
    @Size(min = 6, message = "password minimum size is 6")
    private String password;

    private List<Long> courseIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public List<Long> getCourseIds() {
        return courseIds;
    }

    public void setCourseIds(List<Long> courseIds) {
        this.courseIds = courseIds;
    }
}
