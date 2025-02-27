package com.cloud.recommend.service.datainit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import com.cloud.recommend.entity.RatingEntity;
import com.cloud.recommend.mapper.RatingMapper;

@Service
public class InitRatingService {
    @Autowired
    private RatingMapper ratingMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final int THREAD_POOL_SIZE = 10;
    private final int BATCH_SIZE = 1000;

    public void initializeTable() throws Exception {
        jdbcTemplate.execute("DROP TABLE IF EXISTS ratings");
        ClassPathResource resource = new ClassPathResource("sql/ratings.sql");
        String sql = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        jdbcTemplate.execute(sql);
        importData();
    }

    @Transactional
    public void importData() throws Exception {
        ClassPathResource resource = new ClassPathResource("static/ratings.csv");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            boolean isFirstLine = true;
            List<RatingEntity> ratingsBatch = new ArrayList<>();
            ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (data.length >= 4) {
                    RatingEntity rating = new RatingEntity();
                    rating.setUserId(Long.parseLong(data[0]));
                    rating.setMovieId(Long.parseLong(data[1]));
                    rating.setRating(Double.parseDouble(data[2]));
                    rating.setTimestamp(Long.parseLong(data[3]));
                    ratingsBatch.add(rating);
                }

                if (ratingsBatch.size() >= BATCH_SIZE) {
                    List<RatingEntity> batchToInsert = new ArrayList<>(ratingsBatch);
                    ratingsBatch.clear();
                    executorService.submit(() -> ratingMapper.insertBatch(batchToInsert));
                }
            }

            if (!ratingsBatch.isEmpty()) {
                List<RatingEntity> batchToInsert = new ArrayList<>(ratingsBatch);
                executorService.submit(() -> ratingMapper.insertBatch(batchToInsert));
            }

            executorService.shutdown();
            while (!executorService.isTerminated()) {

            }
        }
    }
}