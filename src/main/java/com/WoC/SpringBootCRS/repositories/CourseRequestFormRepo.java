package com.WoC.SpringBootCRS.repositories;

import com.WoC.SpringBootCRS.entities.CourseRequestForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRequestFormRepo extends JpaRepository<CourseRequestForm, Long> {
}
