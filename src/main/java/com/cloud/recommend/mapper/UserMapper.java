package com.cloud.recommend.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.cloud.recommend.entity.User;

@Mapper
public interface UserMapper {
    void insert(User user);

    User findByUsername(String email);
}