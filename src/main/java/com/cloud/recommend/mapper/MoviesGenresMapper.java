package com.cloud.recommend.mapper;

import com.cloud.recommend.entity.MoviesGenresEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MoviesGenresMapper {
    public void insert(MoviesGenresEntity moviesGenres);

    public List<MoviesGenresEntity> findAll();

    void insertBatch(@Param("moviesGenresBatch") List<MoviesGenresEntity> moviesGenresBatch);

}
