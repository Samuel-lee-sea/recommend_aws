package com.cloud.recommend.mapper;

import com.cloud.recommend.entity.TagsEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagsMapper {
    public void insert(TagsEntity tag);

    public List<TagsEntity> findAll();

    public long getTotalCount();

    void insertBatch(List<TagsEntity> tags);

}