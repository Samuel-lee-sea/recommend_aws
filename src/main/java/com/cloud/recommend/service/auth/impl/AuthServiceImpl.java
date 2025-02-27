package com.cloud.recommend.service.auth.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cloud.recommend.dto.AuthRequest;
import com.cloud.recommend.entity.User;
import com.cloud.recommend.mapper.UserMapper;
import com.cloud.recommend.service.auth.AuthService;
import com.cloud.recommend.utils.JwtUtil;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    private Set<String> invalidatedTokens = new HashSet<>();

    @Override
    public String register(AuthRequest request) throws Exception {
        if (userMapper.findByUsername(request.getEmail()) != null) {
            throw new Exception("Username already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userMapper.insert(user);

        return jwtUtil.generateToken(user.getEmail());
    }

    @Override
    public String login(AuthRequest request) throws Exception {
        User user = userMapper.findByUsername(request.getEmail());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new Exception("Invalid username or password");
        }

        return jwtUtil.generateToken(user.getEmail());
    }

    @Override
    public void logout(String token) {
        invalidatedTokens.add(token);
    }

    public boolean isTokenInvalidated(String token) {
        return invalidatedTokens.contains(token);
    }

}