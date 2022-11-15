package com.backend.application.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
public class JwtAuthentication implements Authentication {

    private String username;

    private Role idRoles;

    private boolean authenticated;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(idRoles);
    }

    @Override
    public Object getCredentials() {
        return HttpStatus.NOT_IMPLEMENTED;
    }

    @Override
    public Object getDetails() {
        return HttpStatus.NOT_IMPLEMENTED;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public String getName() {
        return String.valueOf(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }
}
