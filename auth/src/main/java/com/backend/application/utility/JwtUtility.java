package com.backend.application.utility;

import com.backend.application.domain.JwtAuthentication;
import com.backend.application.domain.Role;
import io.jsonwebtoken.Claims;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class JwtUtility {

    public static Role getRoles(Claims claims) {
        String roles = claims.get("roles", String.class);
        return Role.valueOf(roles);
    }

    public static JwtAuthentication generate(Claims claims) {
        JwtAuthentication jwtAuthentication = new JwtAuthentication();
        jwtAuthentication.setUsername(claims.getSubject());
        jwtAuthentication.setIdRoles(getRoles(claims));
        return jwtAuthentication;
    }
}
