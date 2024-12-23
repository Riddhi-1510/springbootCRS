package com.WoC.SpringBootCRS.repositories;

import com.WoC.SpringBootCRS.entities.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterRepo extends JpaRepository<Semester, Long> {

}
