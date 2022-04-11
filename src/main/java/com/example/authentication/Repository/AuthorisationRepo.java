package com.example.authentication.Repository;

import com.example.authentication.Model.JWTRequest;
import org.springframework.data.jpa.repository.JpaRepository;



public interface AuthorisationRepo extends JpaRepository<JWTRequest,Integer> {

    JWTRequest findByEmail(String email);
}
