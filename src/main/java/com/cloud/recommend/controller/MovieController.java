package com.cloud.recommend.controller;

import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cloud.recommend.constant.ApiResponse;
import com.cloud.recommend.entity.LinksEntity;
import com.cloud.recommend.entity.MovieInfoEntity;
import com.cloud.recommend.entity.RatingAve;
import com.cloud.recommend.service.movie.MovieService;

@RestController
@RequestMapping("/v1")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/movie/{id}")
    public ResponseEntity<Object> getMovieByPathId(@PathVariable Long id) {
        return getMovieById(id);
    }

    @GetMapping("/movie")
    public ResponseEntity<Object> getMovieByQueryParam(@RequestParam Long id) {
        return getMovieById(id);
    }

    private ResponseEntity<Object> getMovieById(Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache");
        try {
            MovieInfoEntity movieEntity = movieService.findMovieInfoById(id);

            if (movieEntity == null) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Movie not found");
                return new ResponseEntity<>(response, headers, HttpStatus.NOT_FOUND);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("movieId", movieEntity.getMovieId());
            response.put("title", movieEntity.getTitle());
            response.put("genres", movieEntity.getGenres());

            return new ResponseEntity<>(response, headers, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse resp = new ApiResponse(503, "Service Unavailable", null);
            return new ResponseEntity<>(resp, headers, 503);
        }
    }

    @GetMapping("/rating/{id}")
    public ResponseEntity<Object> getRatingById(@PathVariable Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache");
        try {
            RatingAve rating = movieService.findRatingById(id);
            if (rating == null) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "No ratings found for this movie");
                return new ResponseEntity<>(response, headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(rating, headers, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse resp = new ApiResponse(503, "Service Unavailable", null);
            return new ResponseEntity<>(resp, headers, 503);
        }
    }

    @GetMapping("/link/{id}")
    public ResponseEntity<Object> getLinksById(@PathVariable Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache");
        try {
            LinksEntity links = movieService.findLinksById(id);
            if (links == null) {
                return new ResponseEntity<>(null, headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(links, headers, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse resp = new ApiResponse(503, "Service Unavailable", null);
            return new ResponseEntity<>(resp, headers, 503);
        }
    }
}