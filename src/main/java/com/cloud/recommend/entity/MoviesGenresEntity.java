package com.cloud.recommend.entity;

import lombok.Data;

@Data
public class MoviesGenresEntity {
    private Long movieId;
    private Integer genreId;

    public MoviesGenresEntity(Long mid, Integer gid) {
        this.movieId = mid;
        this.genreId = gid;
    }
}