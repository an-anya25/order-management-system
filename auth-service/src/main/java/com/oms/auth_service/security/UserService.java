package com.oms.auth_service.security;

import com.oms.auth_service.dto.CreateUserDto;
import com.oms.auth_service.dto.UserDto;
import com.oms.auth_service.entity.User;
import com.oms.auth_service.exception.EmailAlreadyExistsException;
import com.oms.auth_service.exception.UserNotFoundException;
import com.oms.auth_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getLoggedInUser() {
        String email = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        return userRepository.findUserByEmail(email)
                .orElseThrow(()-> new UserNotFoundException("User not found with email: "+email));
    }


    @Transactional
    public UserDto updateCurrentUser(CreateUserDto updateUserDto) {
        User user = getLoggedInUser();

        if(!user.getEmail().equals(updateUserDto.getEmail()) && userRepository.existsByEmail(updateUserDto.getEmail()) ){
            throw new EmailAlreadyExistsException("Email is already in use");
        }

        user.setEmail(updateUserDto.getEmail());
        user.setName(updateUserDto.getName());

        return mapUserToUserDto(user);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map((this::mapUserToUserDto))
                .toList();
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException("User not found with id: "+id));

        return mapUserToUserDto(user);


    }

    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException("User not found with id: "+id));
        userRepository.delete(user);
    }

    public UserDto mapUserToUserDto(User user){
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public UserDto getCurrentUser() {
        return mapUserToUserDto(getLoggedInUser());
    }
}
