package com.WoC.SpringBootCRS.services;

import com.WoC.SpringBootCRS.entities.Admin;
import com.WoC.SpringBootCRS.entities.Professor;
import com.WoC.SpringBootCRS.entities.Student;
import com.WoC.SpringBootCRS.repositories.AdminRepo;
import com.WoC.SpringBootCRS.repositories.ProfessorRepo;
import com.WoC.SpringBootCRS.repositories.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    StudentRepo studentRepo;
    @Autowired
    AdminRepo adminRepo;
    @Autowired
    ProfessorRepo professorRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Student student = studentRepo.findByEmail(email);
        if(student != null ){
            return User.builder()
                    .username(student.getEmail())
                    .password(student.getPassword())
                    .roles("STUDENT")
                    .build();
        }

        Professor professor = professorRepo.findByEmail(email);
        if(professor != null ){
            return User.builder()
                    .username(professor.getEmail())
                    .password(professor.getPassword())
                    .roles("PROFESSOR")
                    .build();
        }

        Admin admin = adminRepo.findByEmail(email);
        if(admin != null ){
            return User.builder()
                    .username(admin.getEmail())
                    .password(admin.getPassword())
                    .roles("ADMIN")
                    .build();
        }

        throw new UsernameNotFoundException("User not found");

    }
}
