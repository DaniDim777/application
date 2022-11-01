package com.backend.application.controller;

import com.backend.application.dto.UserDto;
import com.backend.application.dto.requests.CreateUserRequest;
import com.backend.application.dto.requests.UpdateUserRequest;
import com.backend.application.exceptions.SuchException;
import com.backend.application.exceptions.UserException;
import com.backend.application.mapper.UserMapper;
import com.backend.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private static final String SUCH_USER_EXISTS_MESSAGE = "Such Username Already Exists";
    private static final String USER_NOT_FOUND_MESSAGE = "User Not Found";
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody final CreateUserRequest request) {
        try {
            return userMapper.CONVERT_TO_DTO(userService.createUser(request));
        } catch (DataIntegrityViolationException e) {
            throw new SuchException(SUCH_USER_EXISTS_MESSAGE);
        }
    }

    @GetMapping("/{uuid}")
    @ResponseStatus(code = HttpStatus.OK)
    public UserDto getUser(@PathVariable("uuid") final UUID uuid) {
        return userMapper.CONVERT_TO_DTO(userService.findByUuid(uuid)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND_MESSAGE)));
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public Iterable<UserDto> getAllUsers() {
        return userMapper.CONVERT_IT_TO_DTO(userService.findAll());
    }

    @PutMapping("/{uuid}")
    @ResponseStatus(code = HttpStatus.OK)
    public UserDto updateUser(@Valid @RequestBody final UpdateUserRequest request,
                              @PathVariable final UUID uuid) {
        return userMapper.CONVERT_TO_DTO(userService.updateUser(request, uuid)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND_MESSAGE)));
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("uuid") final UUID uuid) {
        userService.deleteUserByUUID(uuid);
    }
}
