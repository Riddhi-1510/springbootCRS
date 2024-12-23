package com.WoC.SpringBootCRS.repositories;

import com.WoC.SpringBootCRS.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepo extends JpaRepository<Admin, Long> {
    Admin findByEmail(String email);
}
