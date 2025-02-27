package com.cloud.recommend.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cloud.recommend.constant.ApiResponse;
import com.cloud.recommend.service.movie.MovieService;

@RestController
@RequestMapping("/v1")
public class HealthCareController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/healthcheck")
    public ResponseEntity<ApiResponse> healthCheck(@RequestParam Map<String, String> params) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache");
        if (!params.isEmpty()) {
            ApiResponse errorResponse = new ApiResponse(400, "Parameters not allowed", null);
            return new ResponseEntity<>(errorResponse, headers, HttpStatus.BAD_REQUEST);
        }
        try {
            movieService.findMovieInfoById(1L);
            ApiResponse successResponse = new ApiResponse(200, "OK", null);
            return new ResponseEntity<>(successResponse, headers, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse resp = new ApiResponse(503, "Service Unavailable", null);
            return new ResponseEntity<>(resp, headers, 503);
        }

    }

    @RequestMapping(value = "/healthcheck", method = { RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE,
            RequestMethod.PATCH })
    public ResponseEntity<ApiResponse> handleOtherMethods() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache");
        ApiResponse errorResponse = new ApiResponse(400, "Invalid request method", null);
        try {
            movieService.findMovieInfoById(1L);
            return new ResponseEntity<>(errorResponse, headers, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ApiResponse resp = new ApiResponse(503, "Service Unavailable", null);
            return new ResponseEntity<>(resp, headers, 503);
        }
    }

}
