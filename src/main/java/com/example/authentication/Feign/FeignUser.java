package com.example.authentication.Feign;

import com.example.authentication.Model.UserDto;
import com.example.authentication.Model.UserWithOutPassword;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "user-service")
public interface FeignUser {
    @LoadBalanced
    @PostMapping("/api/v1/users")
    UserWithOutPassword createUser(UserDto userDto);
    @GetMapping("/api/v1/users/getUserByEmail/{email}")
    UserWithOutPassword userByEmail(@PathVariable("email") String email);
}
