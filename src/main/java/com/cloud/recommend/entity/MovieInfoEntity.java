package com.cloud.recommend.entity;

import java.util.List;

import lombok.Data;

@Data
public class MovieInfoEntity {
    private Long movieId;
    private String title;
    private List<String> genres;
}
