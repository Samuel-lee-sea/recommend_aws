package com.cloud.recommend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloud.recommend.constant.ApiResponse;
import com.cloud.recommend.service.datainit.InitLinksService;
import com.cloud.recommend.service.datainit.InitMovieService;
import com.cloud.recommend.service.datainit.InitRatingService;
import com.cloud.recommend.service.datainit.InitTagService;
import com.cloud.recommend.service.datainit.InitUserService;

@RestController
@RequestMapping("/v1/data")
public class DataInitController {

    @Autowired
    private InitMovieService initMovie;
    @Autowired
    private InitLinksService initLinksService;
    @Autowired
    private InitRatingService initRatingService;
    @Autowired
    private InitTagService initTagService;
    @Autowired
    private InitUserService initUserService;

    @GetMapping("/init")
    public ResponseEntity<ApiResponse> importData() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache");
        try {
            initUserService.initializeTable();
            initMovie.initializeTable();
            initLinksService.initializeTable();
            initRatingService.initializeTable();
            initTagService.initializeTable();
            ApiResponse resp = new ApiResponse(200, "success", null);
            return new ResponseEntity<>(resp, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse resp = new ApiResponse(503, "Service Unavailable", null);
            return new ResponseEntity<>(resp, headers, 503);
        }
    }

}
