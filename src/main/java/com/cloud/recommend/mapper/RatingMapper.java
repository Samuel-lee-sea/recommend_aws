package com.cloud.recommend.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cloud.recommend.entity.RatingAve;
import com.cloud.recommend.entity.RatingEntity;

@Mapper
public interface RatingMapper {

    public void insert(RatingEntity ratingEntity);

    public long getTotalCount();

    public RatingEntity findByUserIdAndMovieId(int userId, int movieId);

    public List<RatingEntity> findAll();

    void insertBatch(List<RatingEntity> ratings);

    public RatingAve findByMovieId(Long movieId);

}
