package com.backend.application.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignRequest {

    @NotBlank
    @Size(min = 5, max = 32)
    String username;

    @NotBlank
    @Size(min = 5, max = 32)
    String password;
}
