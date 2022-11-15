package com.backend.application.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SavedRefreshService {

    private final Map<String, String> refreshStorage = new ConcurrentHashMap<>();

    public void save(String login, String refreshToken) {
        refreshStorage.put(login, refreshToken);
    }

    public String load(String login) {
        return refreshStorage.get(login);
    }
}
