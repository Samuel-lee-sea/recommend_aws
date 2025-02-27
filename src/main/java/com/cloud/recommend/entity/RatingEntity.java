package com.cloud.recommend.entity;

import lombok.Data;

@Data
public class RatingEntity {
    private Long userId;
    private Long movieId;
    private Double rating;
    private Long timestamp;
}
