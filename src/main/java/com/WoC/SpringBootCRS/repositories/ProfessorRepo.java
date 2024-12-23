package com.WoC.SpringBootCRS.repositories;

import com.WoC.SpringBootCRS.entities.Course;
import com.WoC.SpringBootCRS.entities.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfessorRepo extends JpaRepository<Professor, Long> {
    Professor findByEmail(String email);
}
