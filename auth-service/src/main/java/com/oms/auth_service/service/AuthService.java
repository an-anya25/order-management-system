package com.oms.auth_service.service;

import com.oms.auth_service.dto.CreateUserDto;
import com.oms.auth_service.dto.LoginDto;
import com.oms.auth_service.dto.LoginResponseDto;
import com.oms.auth_service.dto.RegisterUserResponseDto;
import com.oms.auth_service.entity.Role;
import com.oms.auth_service.entity.User;
import com.oms.auth_service.exception.EmailAlreadyExistsException;
import com.oms.auth_service.exception.UserNotFoundException;
import com.oms.auth_service.repository.UserRepository;
import com.oms.auth_service.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public RegisterUserResponseDto registerUser(CreateUserDto createUserDto){
        if(userRepository.existsByEmail(createUserDto.getEmail())){
            throw new EmailAlreadyExistsException("Email is already registered");
        }

        User user = User.builder()
                .name(createUserDto.getName())
                .email(createUserDto.getEmail())
                .password(passwordEncoder.encode(createUserDto.getPassword()))
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);

        return new RegisterUserResponseDto(savedUser.getId(),savedUser.getName());

    }

    public LoginResponseDto loginUser(LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword())
        );

        String email = authentication.getName();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(()->new UserNotFoundException("User not found with email: "+email));

        return new LoginResponseDto(jwtService.generateJwtToken(user));

    }
}
