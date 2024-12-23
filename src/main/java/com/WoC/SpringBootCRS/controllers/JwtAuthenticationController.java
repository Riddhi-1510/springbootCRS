package com.WoC.SpringBootCRS.controllers;

import com.WoC.SpringBootCRS.dtos.AdminDto;
import com.WoC.SpringBootCRS.entities.JwtRequest;
import com.WoC.SpringBootCRS.entities.JwtResponse;
import com.WoC.SpringBootCRS.jwt.JwtHelper;
import com.WoC.SpringBootCRS.services.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class JwtAuthenticationController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtHelper helper;

    @Autowired
    AdminService adminService;

//    public JwtAuthenticationController(UserDetailsService userDetailsService, AuthenticationManager manager, JwtHelper helper) {
//        this.userDetailsService = userDetailsService;
//        this.manager = manager;
//        this.helper = helper;
//    }

//    @PostMapping("/add-admin")
//    public ResponseEntity<?> addAdmin(@RequestBody AdminDto adminDto) {
//        try{
//            adminService.addAdmin(adminDto);
//            return ResponseEntity.ok("Admin added successfully");
//        }catch (Exception e){
//            System.out.println("ERROR: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error adding Admin: " + e.getMessage());
//        }
//    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest request) {

        try{
            this.doAuthenticate(request.getUsername(), request.getPassword());

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            String token = this.helper.generateToken(userDetails);
            JwtResponse response = new JwtResponse(token, userDetails.getUsername());

            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error Login: " + e.getMessage());
        }
    }

    private void doAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Credentials Invalid !!");
        }

    }
}
