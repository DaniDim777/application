package com.backend.application.controller;

import com.backend.application.configuration.UserTestConfiguration;
import com.backend.application.domain.Role;
import com.backend.application.domain.User;
import com.backend.application.dto.UserDto;
import com.backend.application.dto.requests.UpdateUserRequest;
import com.backend.application.exceptions.SuchException;
import com.backend.application.exceptions.UserException;
import com.backend.application.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = UserTestConfiguration.class)
public class UserControllerTest {

    @MockBean
    UserServiceImpl userServiceImpl;

    @InjectMocks
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    @SneakyThrows
    @DisplayName("Проверка создания пользователя")
    public void createUserTest() {

        User user = new User("Alex1", bCryptPasswordEncoder.encode("qwerty"), Role.Intern);

        Mockito.when(userServiceImpl.createUser(Mockito.any())).thenReturn(user);

        mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("Alex1"))
                .andExpect(jsonPath("$.id").value(0))
                .andExpect(jsonPath("$.idRole").value(Role.Intern.name()));
    }


    @Test
    @SneakyThrows
    @DisplayName("Проверка обновления пользователя")
    public void updateUserTest() {

        User user1 = new User("Alex1", bCryptPasswordEncoder.encode("qwerty"), Role.Mentor);
        User user2 = new User("John9", bCryptPasswordEncoder.encode("newpass"), Role.Admin);

        Mockito.when(userServiceImpl.findByUuid(Mockito.any())).thenReturn(Optional.of(user1));
        Mockito.when(userServiceImpl.updateUser(Mockito.any(), Mockito.any())).thenReturn(Optional.of(user2));

        mockMvc.perform(put("/users/" + user1.getUuid())
                .content(objectMapper.writeValueAsString(new UpdateUserRequest("John9", "newpass", Role.Admin)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("John9"))
                .andExpect(jsonPath("$.idRole").value(Role.Admin.name()));
    }

    @Test
    @SneakyThrows
    @DisplayName("Проверка удаления пользователя")
    public void deleteUserTest() {

        User user = new User("Alex1", bCryptPasswordEncoder.encode("qwerty"), Role.Intern);

        Mockito.when(userServiceImpl.findByUuid(Mockito.any())).thenReturn(Optional.of(user));

        mockMvc.perform(delete("/users/" + user.getUuid()))
                .andExpect(status().isNoContent());
    }

    @Test
    @SneakyThrows
    @DisplayName("Провера получения пользователя")
    public void getUserTest() {

        User user = new User("Alex1", bCryptPasswordEncoder.encode("qwerty"), Role.Intern);

        Mockito.when(userServiceImpl.findByUuid(Mockito.any())).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/" + user.getUuid())
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("Alex1"))
                .andExpect(jsonPath("$.idRole").value(Role.Intern.name()));
    }

    @Test
    @SneakyThrows
    @DisplayName("Проверка получения всех пользователей")
    public void getAllUsersTest() {

        User user1 = new User("Alex1", bCryptPasswordEncoder.encode("qwerty"), Role.Mentor);
        User user2 = new User("Tom55", bCryptPasswordEncoder.encode("password"), Role.Supervisor);

        Mockito.when(userServiceImpl.findAll()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/users/"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(
                        new UserDto(user1.getId(), user1.getUuid(), user1.getUsername(),
                                user1.getIdRole(), user1.getCreated(), user1.getModified()),
                        new UserDto(user2.getId(), user2.getUuid(), user2.getUsername(),
                                user2.getIdRole(), user2.getCreated(), user2.getModified())))));
    }

    @Test
    @SneakyThrows
    @DisplayName("Проверка создания уже существующего пользователя")
    public void createUserTestException() {

        User user1 = new User("Alex1", bCryptPasswordEncoder.encode("qwe"), Role.Intern);
        User user2 = new User("Alex1", bCryptPasswordEncoder.encode("qwe"), Role.Intern);

        Mockito.when(userServiceImpl.createUser(Mockito.any())).thenReturn(user1);
        Mockito.when(userServiceImpl.createUser(Mockito.any())).thenThrow(SuchException.class);
        mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(user2))
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isConflict())
//                .andExpect(jsonPath("$.application_info").value("Such User Already Exists"));
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof SuchException));
    }

    @Test
    @SneakyThrows
    @DisplayName("Проверка обновления несуществующего пользователя")
    public void updateUserTestException() {

        Mockito.when(userServiceImpl.findByUuid(Mockito.any())).thenReturn(Optional.empty());

        mockMvc.perform(put("/users/" + UUID.randomUUID())
                .content(objectMapper.writeValueAsString(new UpdateUserRequest("Alex1", "qwerty", Role.Intern)))
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.application_info").value("User Not Found"))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserException));
    }

    @Test
    @SneakyThrows
    @DisplayName("Проверка удаления несуществующего пользователя")
    public void deleteUserTestException() {

        Mockito.when(userServiceImpl.findByUuid(Mockito.any())).thenReturn(null);

        mockMvc.perform(delete("/users/" + UUID.randomUUID()))
                .andExpect(status().isNoContent());
    }

    @Test
    @SneakyThrows
    @DisplayName("Проверка получения несуществующего пользователя")
    public void getUserTestException() {

        Mockito.when(userServiceImpl.findByUuid(Mockito.any())).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.application_info").value("User Not Found"))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserException));
    }

    @Test
    @SneakyThrows
    @DisplayName("Провера получения пустого репозитория")
    public void getAllUsersEmptyTest() {

        Mockito.when(userServiceImpl.findAll()).thenReturn(null);

        mockMvc.perform(get("/users/"))
                .andExpect(status().isOk());
    }
}
