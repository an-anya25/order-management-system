package com.oms.auth_service.controller;

import com.oms.auth_service.dto.CreateUserDto;
import com.oms.auth_service.dto.LoginDto;
import com.oms.auth_service.dto.LoginResponseDto;
import com.oms.auth_service.dto.RegisterUserResponseDto;
import com.oms.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponseDto> registerUser(@Valid @RequestBody CreateUserDto createUserDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(createUserDto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginUser(@Valid @RequestBody LoginDto loginDto){
        return ResponseEntity.ok(authService.loginUser(loginDto));
    }
}
