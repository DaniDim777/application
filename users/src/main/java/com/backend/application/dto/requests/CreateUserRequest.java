package com.backend.application.dto.requests;

import com.backend.application.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 2, max = 32)
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 2, max = 62)
    private String password;

    private Role idRole;
}
