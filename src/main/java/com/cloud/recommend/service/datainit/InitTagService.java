package com.cloud.recommend.service.datainit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import com.cloud.recommend.entity.TagsEntity;
import com.cloud.recommend.mapper.TagsMapper;

@Service
public class InitTagService {
    @Autowired
    private TagsMapper tagsMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void initializeTable() throws Exception {
        jdbcTemplate.execute("DROP TABLE IF EXISTS tags");
        ClassPathResource resource = new ClassPathResource("sql/tags.sql");
        String sql = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        jdbcTemplate.execute(sql);
        importData();
    }

    @Transactional(timeout = 300)
    public void importData() throws Exception {
        ClassPathResource resource = new ClassPathResource("static/tags.csv");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            boolean isFirstLine = true;
            List<TagsEntity> tagsBatch = new ArrayList<>();
            int batchSize = 500;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (data.length >= 4) {
                    Integer userId = Integer.parseInt(data[0]);
                    Integer movieId = Integer.parseInt(data[1]);
                    String tag = data[2];
                    Integer timestamp = Integer.parseInt(data[3]);
                    TagsEntity tagsEntity = new TagsEntity();
                    tagsEntity.setUserId(userId);
                    tagsEntity.setMovieId(movieId);
                    tagsEntity.setTag(tag);
                    tagsEntity.setTimestamp(timestamp);
                    tagsBatch.add(tagsEntity);
                }

                if (tagsBatch.size() >= batchSize) {
                    tagsMapper.insertBatch(tagsBatch);
                    tagsBatch.clear();
                }
            }

            if (!tagsBatch.isEmpty()) {
                tagsMapper.insertBatch(tagsBatch);
            }
        }
    }
}