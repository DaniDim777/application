package com.backend.application.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "\"users\"")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "hibernate_sequence_user", allocationSize = 1)
    @Column(name = "\"id\"", nullable = false)
    private int id;

    @Column(name = "\"uuid\"", nullable = false, unique = true)
    private UUID uuid;

    @NotBlank
    @Column(name = "\"username\"", nullable = false, unique = true)
    private String username;

    @NotBlank
    @Column(name = "\"password\"", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "\"id_role\"", nullable = false)
    private Role idRole;

    @CreatedDate
    @Column(name = "\"created\"", updatable = false)
    private LocalDateTime created = LocalDateTime.now();

    @LastModifiedDate
    @Column(name = "\"modified\"")
    private LocalDateTime modified = LocalDateTime.now();

    public User(String username, String password, Role idRole) {
        this.uuid = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.idRole = idRole;
    }
}
