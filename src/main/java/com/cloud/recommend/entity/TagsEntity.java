package com.cloud.recommend.entity;

import lombok.Data;

@Data
public class TagsEntity {
    private Integer userId;
    private Integer movieId;
    private String tag;
    private Integer timestamp;
}