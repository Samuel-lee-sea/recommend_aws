package com.cloud.recommend.entity;

import lombok.Data;

@Data
public class LinksEntity {
    private Integer movieId;
    private String imdbId;
    private String tmdbId;
}