package com.backend.application.filter;

import com.backend.application.domain.JwtAuthentication;
import com.backend.application.service.JwtProvider;
import com.backend.application.utility.JwtUtility;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private static final String AUTHORIZATION = "Authorization";
    private final JwtProvider jwtProvider;

    private Optional<String> getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return Optional.of(bearer.substring(7));
        }
        return Optional.empty();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        Optional<String> token = getTokenFromRequest((HttpServletRequest) servletRequest);
        if (token.isPresent() && jwtProvider.validateAccessToken(token.get())) {
            Claims claims = jwtProvider.getAccessClaims(token.get());
            JwtAuthentication jwtAuthentication = JwtUtility.generate(claims);
            jwtAuthentication.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
