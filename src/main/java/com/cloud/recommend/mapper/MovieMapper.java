package com.cloud.recommend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cloud.recommend.entity.MovieEntity;

import java.util.List;

@Mapper
public interface MovieMapper {
    void insert(MovieEntity movie);

    List<MovieEntity> findAll();

    MovieEntity findById(Long id);

    long getTotalCount();

    void insertBatch(@Param("movies") List<MovieEntity> movies);
}