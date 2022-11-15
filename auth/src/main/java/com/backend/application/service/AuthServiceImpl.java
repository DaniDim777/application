package com.backend.application.service;

import com.backend.application.domain.User;
import com.backend.application.dto.JwtResponse;
import com.backend.application.dto.requests.SignRequest;
import com.backend.application.exceptions.AuthException;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final SavedRefreshService savedRefreshService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public JwtResponse sign(@NonNull SignRequest signRequest) {
        User user = userService.findByUsername(signRequest.getUsername())
                .orElseThrow(() -> new AuthException(HttpStatus.UNAUTHORIZED));
        if (bCryptPasswordEncoder.matches(signRequest.getPassword(), user.getPassword())) {
            String accessToken = jwtProvider.generateAccessToken(user);
            String refreshToken = jwtProvider.generateRefreshToken(user);
            savedRefreshService.save(user.getUsername(), refreshToken);
            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new AuthException(HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public Optional<JwtResponse> getAccessToken(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            String login = claims.getSubject();
            String savedRefreshToken = savedRefreshService.load(login);
            if (savedRefreshToken != null && savedRefreshToken.equals(refreshToken)) {
                User user = userService.findByUsername(login)
                        .orElseThrow(() -> new AuthException(HttpStatus.UNAUTHORIZED));
                String newAccessToken = jwtProvider.generateAccessToken(user);
                return Optional.of(new JwtResponse(newAccessToken, null));
            }
        }
        return Optional.ofNullable(new JwtResponse(null, null));
    }

    @Override
    public JwtResponse getAllNewTokens(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            String login = claims.getSubject();
            String savedRefreshToken = savedRefreshService.load(login);
            if (savedRefreshToken != null && savedRefreshToken.equals(refreshToken)) {
                User user = userService.findByUsername(login)
                        .orElseThrow(() -> new AuthException(HttpStatus.UNAUTHORIZED));
                String newAccessToken = jwtProvider.generateAccessToken(user);
                String newRefreshToken = jwtProvider.generateRefreshToken(user);
                savedRefreshService.save(user.getUsername(), newRefreshToken);
                return new JwtResponse(newAccessToken, newRefreshToken);
            }
        }
        throw new AuthException(HttpStatus.FORBIDDEN);
    }
}
