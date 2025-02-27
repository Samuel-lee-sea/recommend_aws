package com.cloud.recommend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RatingAve {

    private Long movieId;
    @JsonProperty("average_rating")
    private Double averageRating;

}
