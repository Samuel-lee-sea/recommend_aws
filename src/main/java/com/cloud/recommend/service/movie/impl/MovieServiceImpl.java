package com.cloud.recommend.service.movie.impl;

import com.cloud.recommend.entity.LinksEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.recommend.entity.MovieEntity;
import com.cloud.recommend.entity.MovieInfoEntity;
import com.cloud.recommend.entity.RatingAve;
import com.cloud.recommend.mapper.LinksMapper;
import com.cloud.recommend.mapper.MovieInfoMapper;
import com.cloud.recommend.mapper.MovieMapper;
import com.cloud.recommend.mapper.RatingMapper;
import com.cloud.recommend.service.movie.MovieService;

@Service
public class MovieServiceImpl implements MovieService {
    @Autowired
    private MovieMapper movieMapper;
    @Autowired
    private MovieInfoMapper movieInfoMapper;
    @Autowired
    private RatingMapper ratingMapper;
    @Autowired
    private LinksMapper linksMapper;

    public MovieEntity findMovieById(Long movieId) throws Exception {
        MovieEntity movie = movieMapper.findById(movieId);
        return movie;
    }

    @Override
    public MovieInfoEntity findMovieInfoById(Long movieId) throws Exception {
        return movieInfoMapper.getMovieWithGenres(movieId);
    }

    @Override
    public RatingAve findRatingById(Long movieId) throws Exception {
        return ratingMapper.findByMovieId(movieId);
    }

    @Override
    public LinksEntity findLinksById(Long id) {
        return linksMapper.findLinksById(id);
    }

}
