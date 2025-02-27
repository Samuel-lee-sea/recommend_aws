package com.cloud.recommend.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.cloud.recommend.entity.MovieInfoEntity;

@Mapper
public interface MovieInfoMapper {
    MovieInfoEntity getMovieWithGenres(Long movieId);
}
