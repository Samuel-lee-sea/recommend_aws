package com.cloud.recommend.controller;

import com.cloud.recommend.constant.ApiResponse;
import com.cloud.recommend.dto.AuthRequest;
import com.cloud.recommend.service.auth.AuthService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody AuthRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache");
        try {
            System.out.println("Received registration request for email: " + request.getEmail());

            if (request.getEmail() == null || request.getEmail().isEmpty()) {
                System.out.println("Email is empty");
                ApiResponse resp = new ApiResponse(400, "Email cannot be empty", null);
                return new ResponseEntity<>(resp, headers, 400);
            }

            if (request.getPassword() == null || request.getPassword().isEmpty()) {
                System.out.println("Password is empty");
                ApiResponse resp = new ApiResponse(400, "Password cannot be empty", null);
                return new ResponseEntity<>(resp, headers, 400);
            }

            String token = authService.register(request);
            System.out.println("Registration successful for: " + request.getEmail());

            Map<String, String> data = new HashMap<>();
            data.put("token", token);
            ApiResponse resp = new ApiResponse(201, "Registration successful", data);
            return new ResponseEntity<>(resp, headers, 201);
        } catch (Exception e) {
            System.out.println("Registration failed with error: " + e.getMessage());
            e.printStackTrace();
            ApiResponse resp = new ApiResponse(401, "Authorization failed", e.getMessage());
            return new ResponseEntity<>(resp, headers, 401);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody AuthRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache");
        try {
            String token = authService.login(request);
            Map<String, String> data = new HashMap<>();
            data.put("token", token);
            return new ResponseEntity<>(data, headers, 200);
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
            ApiResponse resp = new ApiResponse(401, "Authorization failed", e.getMessage());
            return new ResponseEntity<>(resp, headers, 401);
        }
    }

    // @PostMapping("/logout")
    // public ResponseEntity<ApiResponse> logout(@RequestHeader("Authorization")
    // String authHeader) {
    // HttpHeaders headers = new HttpHeaders();
    // headers.add("Cache-Control", "no-cache");
    // try {
    // String token = authHeader.substring(7);
    // authService.logout(token);
    // return ResponseEntity.ok().headers(headers).body(new ApiResponse(200,
    // "Logoutsuccessful", null));
    // } catch (Exception e) {
    // return ResponseEntity.badRequest().headers(headers)
    // .body(new ApiResponse(400, e.getMessage(), e.getMessage()));
    // }
    // }
}