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
import com.cloud.recommend.entity.LinksEntity;
import com.cloud.recommend.mapper.LinksMapper;

@Service
public class InitLinksService {
    @Autowired
    private LinksMapper linksMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void initializeTable() throws Exception {
        jdbcTemplate.execute("DROP TABLE IF EXISTS links");
        ClassPathResource resource = new ClassPathResource("sql/links.sql");
        String sql = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        jdbcTemplate.execute(sql);
        importData();
    }

    @Transactional
    public void importData() throws Exception {
        ClassPathResource resource = new ClassPathResource("static/links.csv");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            boolean isFirstLine = true;
            List<LinksEntity> linksBatch = new ArrayList<>();
            int batchSize = 1000;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (data.length >= 3) {
                    LinksEntity link = new LinksEntity();
                    link.setMovieId(Integer.parseInt(data[0]));
                    link.setImdbId(data[1].replaceAll("\"", ""));
                    link.setTmdbId(data[2].replaceAll("\"", ""));
                    linksBatch.add(link);
                }

                if (linksBatch.size() >= batchSize) {
                    linksMapper.insertBatch(linksBatch);
                    linksBatch.clear();
                }
            }

            if (!linksBatch.isEmpty()) {
                linksMapper.insertBatch(linksBatch);
            }

        }
    }
}
