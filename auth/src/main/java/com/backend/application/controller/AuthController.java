package com.backend.application.controller;

import com.backend.application.dto.JwtResponse;
import com.backend.application.dto.RefreshJwtRequest;
import com.backend.application.dto.requests.SignRequest;
import com.backend.application.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign")
    public JwtResponse sign(@Valid @RequestBody SignRequest signRequest) {
        return authService.sign(signRequest);
    }

    @PostMapping("/access")
    public Optional<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest refreshJwtRequest) {
        return authService.getAccessToken(refreshJwtRequest.getRefreshToken());
    }

    @PostMapping("/refresh")
    public JwtResponse getAllNewTokens(@RequestBody RefreshJwtRequest refreshJwtRequest) {
        return authService.getAllNewTokens(refreshJwtRequest.getRefreshToken());
    }
}
