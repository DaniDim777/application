package com.backend.application.service;

import com.backend.application.domain.User;
import com.backend.application.dto.requests.CreateUserRequest;
import com.backend.application.dto.requests.UpdateUserRequest;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    User createUser(CreateUserRequest request);
    Optional<User> findByUuid(UUID uuid);
    Iterable<User> findAll();
    Optional<User> updateUser(UpdateUserRequest request, UUID uuid);
    void deleteUserByUUID(UUID uuid);
}
