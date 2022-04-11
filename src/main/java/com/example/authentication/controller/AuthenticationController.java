package com.example.authentication.controller;


import com.example.authentication.Feign.FeignUser;
import com.example.authentication.Model.*;
import com.example.authentication.Repository.AuthorisationRepo;
import com.example.authentication.Utility.JWTUtility;
import com.example.authentication.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
@CrossOrigin(value="*")
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    FeignUser feignUser;

    @Autowired
    AuthorisationRepo authorisationRepo;

    @CrossOrigin(value = "*")
    @PostMapping("/login")
    public ResponseEntity<JWTResponse> login(@RequestBody LoginRequest loginRequest) throws Exception {

             authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        final UserDetails userDetails = authenticationService.loadUserByUsername(loginRequest.getEmail());
        final String token = jwtUtility.generateToken(userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(new JWTResponse(token, feignUser.userByEmail(loginRequest.getEmail())));
    }

    @CrossOrigin(value = "*")
    @PostMapping("/signup")
    public ResponseEntity<JWTResponse> signup(@RequestBody UserDto userDto) throws Exception {
        JWTRequest jwtRequest = new JWTRequest();
        jwtRequest.setEmail(userDto.getEmail());
        jwtRequest.setPassword(userDto.getPassword());


        authorisationRepo.save(jwtRequest);
        UserWithOutPassword userWithOutPassword = feignUser.createUser(userDto);
        final UserDetails userDetails = new User(userDto.getEmail(),new BCryptPasswordEncoder().encode(userDto.getPassword()), new ArrayList<>());
        final String token = jwtUtility.generateToken(userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(new JWTResponse(token,userWithOutPassword));
    }
}
