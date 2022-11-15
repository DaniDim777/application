package com.backend.application.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RefreshJwtRequest {

    @NotBlank
    private String refreshToken;
}
