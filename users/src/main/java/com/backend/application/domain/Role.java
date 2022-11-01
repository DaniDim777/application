package com.backend.application.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum Role implements GrantedAuthority {

    Intern("INTERN"),
    Mentor("MENTOR"),
    Admin("ADMIN"),
    Supervisor("SUPERVISOR");

    private final String value;

    @Override
    public String getAuthority() {
        return value;
    }
}
