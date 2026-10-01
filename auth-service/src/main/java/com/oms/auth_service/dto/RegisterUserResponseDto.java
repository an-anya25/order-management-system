package com.oms.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterUserResponseDto {

    private Long id;
    private String name;
}
