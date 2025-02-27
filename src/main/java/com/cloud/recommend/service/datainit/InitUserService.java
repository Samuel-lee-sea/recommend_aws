package com.cloud.recommend.service.datainit;

import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

@Service
public class InitUserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public long initializeTable() throws Exception {
        jdbcTemplate.execute("DROP TABLE IF EXISTS user");
        ClassPathResource resource = new ClassPathResource("sql/user.sql");
        String sql = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        jdbcTemplate.execute(sql);
        return 0;
    }
}
