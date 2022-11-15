package com.backend.application.service;

import com.backend.application.domain.Role;
import com.backend.application.domain.User;
import com.backend.application.dto.requests.CreateUserRequest;
import com.backend.application.dto.requests.UpdateUserRequest;
import com.backend.application.exceptions.SuchException;
import com.backend.application.exceptions.UserException;
import com.backend.application.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    UserServiceImpl userServiceImpl;

    @Test
    @DisplayName("Проверка создания пользователя")
    public void createUserTest() {

        User user1 = new User("Alex1", bCryptPasswordEncoder.encode("qwerty"), Role.Intern);
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user1);

        User user2 = userServiceImpl.createUser(new CreateUserRequest("Alex1", "qwerty"));
        assertThat(user1).isEqualTo(user2);
    }

    @Test
    @DisplayName("Проверка обновления пользователя")
    public void updateUserTest() {

        User user1 = new User("Alex1", bCryptPasswordEncoder.encode("qwerty"), Role.Intern);
        User user2 = new User("Dan55", bCryptPasswordEncoder.encode("password"), Role.Mentor);

        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user2);
        Mockito.when(userRepository.findByUuid(Mockito.any())).thenReturn(Optional.of(user1));

        userServiceImpl.updateUser(new UpdateUserRequest("Dan55", "password", Role.Mentor), user1.getUuid());
        assertThat(Optional.of(user1.getUsername())).isEqualTo(Optional.of(user2.getUsername()));
        assertThat(Optional.of(user1.getIdRole())).isEqualTo(Optional.of(user2.getIdRole()));
        assertThat(Optional.of(bCryptPasswordEncoder.matches("qwerty", user1.getPassword())))
                .isEqualTo(Optional.of(bCryptPasswordEncoder.matches("qwerty", user2.getPassword())));
    }

    @Test
    @DisplayName("Проверка удаления пользователя")
    public void deleteUserTest() {

        User user = new User("Alex1", bCryptPasswordEncoder.encode("qwerty"), Role.Intern);

        Mockito.when(userRepository.findByUuid(Mockito.any())).thenReturn(Optional.of(user));
        userServiceImpl.deleteUserByUUID(user.getUuid());

        Mockito.verify(userRepository).delete(user);
        assertThat(userRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("Проверка получения пользователя")
    public void findByUUIDTest() {

        User user = new User("Alex1", bCryptPasswordEncoder.encode("qwerty"), Role.Intern);
        Mockito.when(userRepository.findByUuid(Mockito.any())).thenReturn(Optional.of(user));

        User userFound = userServiceImpl.findByUuid(user.getUuid()).orElseThrow();
        assertThat(userFound).isEqualTo(user);
    }


    @Test
    @DisplayName("Проверка получения всех пользователей")
    public void findAllTest() {

        User user1 = new User("Alex1", bCryptPasswordEncoder.encode("qwerty"), Role.Intern);
        User user2 = new User("Dan55", bCryptPasswordEncoder.encode("password"), Role.Mentor);

        Mockito.when(userServiceImpl.findAll()).thenReturn(List.of(user1, user2));
        Iterable<User> users = userServiceImpl.findAll();
        assertThat(users).isEqualTo(List.of(user1, user2));
    }

    @Test
    @DisplayName("Проверка создания уже существующего пользователя")
    public void createUserTestException() {

        User user = new User("Tom55", bCryptPasswordEncoder.encode("password"), Role.Intern);

        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        Mockito.when(userRepository.save(Mockito.any())).thenThrow(SuchException.class);
        Throwable throwable = catchThrowable(() -> userServiceImpl
                .createUser(new CreateUserRequest("Tom55", "password")));
        assertThat(throwable).isInstanceOf(SuchException.class);
    }

    @Test
    @DisplayName("Проверка обновления несуществующего пользователя")
    public void updateUserTestException() {

        Mockito.when(userServiceImpl.findByUuid(Mockito.any())).thenThrow(UserException.class);
        Throwable throwable = catchThrowable(() -> userServiceImpl
                .updateUser(new UpdateUserRequest("Alex1", "qwerty", Role.Mentor), UUID.randomUUID()));
        assertThat(throwable).isInstanceOf(UserException.class);
    }

    @Test
    @DisplayName("Проверка удаления несуществующего пользователя")
    public void deleteUserTestException() {

        Mockito.when(userServiceImpl.findByUuid(Mockito.any())).thenThrow(UserException.class);
        Throwable throwable = catchThrowable(() -> userServiceImpl
                .deleteUserByUUID(UUID.randomUUID()));
        assertThat(throwable).isInstanceOf(UserException.class);
    }

    @Test
    @DisplayName("Проверка получения несуществующего пользователя по UUID")
    public void findByUUIDTestException() {

        Mockito.when(userServiceImpl.findByUuid(Mockito.any())).thenThrow(UserException.class);
        Throwable throwable = catchThrowable(() -> userServiceImpl
                .findByUuid(UUID.randomUUID()));
        assertThat(throwable).isInstanceOf(UserException.class);
    }

    @Test
    @DisplayName("Проверка пустого репозитория")
    public void findAllEmptyTest() {

        Iterable<User> users = userServiceImpl.findAll();
        assertThat(users).isEmpty();
    }
}
