package com.backend.application.service;

import com.backend.application.domain.User;
import com.backend.application.dto.requests.CreateUserRequest;
import com.backend.application.dto.requests.UpdateUserRequest;
import com.backend.application.exceptions.UserException;
import com.backend.application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String USER_NOT_FOUND_MESSAGE = "User Not Found";
    private final UserRepository userRepository;

    @Transactional
    @Override
    public User createUser(CreateUserRequest request) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return userRepository.save(new User(request.getUsername(),
                bCryptPasswordEncoder.encode(request.getPassword()), request.getIdRole()));
    }

    @Transactional
    @Override
    public Optional<User> updateUser(UpdateUserRequest request, UUID uuid) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Optional<User> optionalUser = userRepository.findByUuid(uuid);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUsername(request.getUsername());
            user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
            user.setIdRole(request.getIdRole());
            return Optional.of(userRepository.save(user));
        }
        return Optional.empty();
    }

    @Transactional
    @Override
    public void deleteUserByUUID(UUID uuid) {
        userRepository.delete(userRepository.findByUuid(uuid)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND_MESSAGE)));
    }

    @Override
    public Optional<User> findByUuid(UUID uuid) {
        return userRepository.findByUuid(uuid);
    }

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }
}
