package com.cloud.recommend.mapper;

import com.cloud.recommend.entity.GenreEntity;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GenreMapper {
    void insert(String genre);

    List<GenreEntity> findAll();

    GenreEntity findById(int genreId);

    Integer findByName(String genre);
}