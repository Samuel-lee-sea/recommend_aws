package com.cloud.recommend.mapper;

import com.cloud.recommend.entity.LinksEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinksMapper {
    public void insert(LinksEntity link);

    public List<LinksEntity> findAll();

    public long getTotalCount();

    void insertBatch(List<LinksEntity> links);

    LinksEntity findLinksById(Long movieId);

}
