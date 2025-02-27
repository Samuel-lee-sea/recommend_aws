package com.cloud.recommend.service.auth;

import com.cloud.recommend.dto.AuthRequest;

public interface AuthService {
    String register(AuthRequest request) throws Exception;

    String login(AuthRequest request) throws Exception;

    void logout(String token);
}