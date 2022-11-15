package com.backend.application.service;

import com.backend.application.dto.JwtResponse;
import com.backend.application.dto.requests.SignRequest;
import lombok.NonNull;

import java.util.Optional;

public interface AuthService {

    JwtResponse sign(@NonNull SignRequest signRequest);

    Optional<JwtResponse> getAccessToken(@NonNull String refreshToken);

    JwtResponse getAllNewTokens(@NonNull String refreshToken);
}
