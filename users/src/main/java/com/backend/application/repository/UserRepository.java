package com.backend.application.repository;

import com.backend.application.domain.Role;
import com.backend.application.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {

    Optional<User> findByUuid(UUID uuid);

    Optional<User> findByUsername(String username);

    Optional<User> findByIdRole(Role idRole);
}
