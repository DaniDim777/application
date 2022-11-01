package com.backend.application.dto;

import com.backend.application.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private int id;
    private UUID uuid;
    private String username;
    private Role idRole;
    private LocalDateTime created;
    private LocalDateTime modified;
}
