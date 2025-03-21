package com.WoC.SpringBootCRS.repositories;

import com.WoC.SpringBootCRS.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepo extends JpaRepository<Student, Long> {

    @Query("SELECT email FROM Student")
    List<String> findAllEmails();
    Student findByEmail(String email);
}
