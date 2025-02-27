package com.cloud.recommend.service.datainit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import com.cloud.recommend.entity.MovieEntity;
import com.cloud.recommend.entity.MoviesGenresEntity;
import com.cloud.recommend.mapper.MovieMapper;
import com.cloud.recommend.mapper.GenreMapper;
import com.cloud.recommend.mapper.MoviesGenresMapper;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InitMovieService {
    @Autowired
    private MovieMapper movieMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private GenreMapper genresMapper;
    @Autowired
    private MoviesGenresMapper moviesGenresMapper;

    public void initializeTable() throws Exception {
        jdbcTemplate.execute("DROP TABLE IF EXISTS movies_genres");
        jdbcTemplate.execute("DROP TABLE IF EXISTS movies");
        jdbcTemplate.execute("DROP TABLE IF EXISTS genres");

        ClassPathResource resource = new ClassPathResource("sql/movie.sql");
        String sql = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        jdbcTemplate.execute(sql);

        resource = new ClassPathResource("sql/genres.sql");
        sql = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        jdbcTemplate.execute(sql);

        resource = new ClassPathResource("sql/movie_genres.sql");
        sql = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        jdbcTemplate.execute(sql);

        importData();
    }

    @Transactional
    public void importData() throws Exception {
        ClassPathResource resource = new ClassPathResource("static/movies.csv");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            boolean isFirstLine = true;
            Map<String, Integer> genreMap = new HashMap<>();
            List<MovieEntity> movieBatch = new ArrayList<>();
            List<MoviesGenresEntity> moviesGenersBatch = new ArrayList<>();
            int batchSize = 1000;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (data.length >= 3) {
                    MovieEntity movie = new MovieEntity();
                    movie.setMovieId(Long.valueOf(data[0]));
                    movie.setTitle(data[1].replaceAll("\"", ""));
                    movieBatch.add(movie);

                    String[] genres = data[2].replaceAll("\"", "").split("\\|");
                    for (String genre : genres) {
                        if (!genreMap.containsKey(genre)) {
                            genresMapper.insert(genre);
                            genreMap.put(genre, genresMapper.findByName(genre));
                        }
                        moviesGenersBatch.add(new MoviesGenresEntity(movie.getMovieId(), genreMap.get(genre)));
                    }

                    if (movieBatch.size() >= batchSize) {
                        movieMapper.insertBatch(movieBatch);
                        moviesGenresMapper.insertBatch(moviesGenersBatch);
                        movieBatch.clear();
                        moviesGenersBatch.clear();
                    }
                }
            }

            if (!movieBatch.isEmpty()) {
                movieMapper.insertBatch(movieBatch);
            }
            if (!moviesGenersBatch.isEmpty()) {
                moviesGenresMapper.insertBatch(moviesGenersBatch);
            }
        }
    }
}