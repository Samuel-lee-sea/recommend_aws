package com.cloud.recommend.entity;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String email;
    private String password;
}
