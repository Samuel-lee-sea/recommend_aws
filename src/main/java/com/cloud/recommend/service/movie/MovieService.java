package com.cloud.recommend.service.movie;

import com.cloud.recommend.entity.LinksEntity;
import com.cloud.recommend.entity.MovieEntity;
import com.cloud.recommend.entity.MovieInfoEntity;
import com.cloud.recommend.entity.RatingAve;

public interface MovieService {
    public MovieEntity findMovieById(Long movieId) throws Exception;

    MovieInfoEntity findMovieInfoById(Long movieId) throws Exception;

    RatingAve findRatingById(Long movieId) throws Exception;

    public LinksEntity findLinksById(Long id);
}
