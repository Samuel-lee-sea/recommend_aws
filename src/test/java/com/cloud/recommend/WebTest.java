package com.cloud.recommend;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class WebTest {

    @Value("${app.baseurl}")
    private String BASE_URL;
    private static RestTemplate restTemplate;
    private static PrintStream logStream;

    @BeforeAll
    public static void setup() throws FileNotFoundException {
        logStream = new PrintStream(new FileOutputStream("log/test-log.txt", true));
        System.setOut(logStream);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);

        restTemplate = new RestTemplate(factory);
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError();
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                    
                } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                    
                } else if (response.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                    
                } else if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    
                } else {
                    new DefaultResponseErrorHandler().handleError(response);
                }
            }
        });
    }

    private static void log(String message) {
        logStream.println(message);
    }

    private ResponseEntity<Map> sendRequest(String endpoint, HttpMethod method, String token, String body, int expectedStatus) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (token != null && !token.isEmpty()) {
            headers.set("Authorization", "Bearer " + token);
        }

        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(BASE_URL + endpoint, method, entity, Map.class);
        log("[RESPONSE] " + response.getStatusCode());
        assertEquals(expectedStatus, response.getStatusCodeValue(), "[ERROR] Unexpected HTTP status for " + endpoint);
        return response;
    }

    @Test
    public void testHealthCheck() {
        log("[TEST] - 2A: [GET] /v1/healthcheck... ");
        sendRequest("/healthcheck", HttpMethod.GET, "", "", 200);
        log("2A test passed.");
    }

    @Test
    public void testHealthCheckInvalidParam() {
        log("[TEST] - 2B: [GET] /v1/healthcheck?test=test with parameter... ");
        sendRequest("/healthcheck?test=test", HttpMethod.GET, "", "", 400);
        log("2B test passed.");
    }

    @Test
    public void testHealthCheckInvalidMethod() {
        log("[TEST] - 2C: /v1/healthcheck with invalid method... ");
        sendRequest("/healthcheck", HttpMethod.POST, "", "", 400);
        sendRequest("/healthcheck", HttpMethod.PUT, "", "", 400);
        sendRequest("/healthcheck", HttpMethod.DELETE, "", "", 400);
        sendRequest("/healthcheck", HttpMethod.PATCH, "", "", 400);
        log("2C test passed.");
    }

    @Test
    public void testRegisterAndLogin() {
        log("[TEST] - 4A: Check [POST] /v1/register endpoint ... ");

        String email = "testuser" + System.currentTimeMillis() + "@example.com";
        String requestBody = String.format("{\"email\":\"%s\", \"password\":\"testpassword\"}", email);
        sendRequest("/register", HttpMethod.POST, "", requestBody, 201);

        log("[TEST] - 4A: Check [POST] /v1/login endpoint ... ");
        ResponseEntity<Map> response = sendRequest("/login", HttpMethod.POST, "", requestBody, 200);

        assertNotNull(response.getBody(), "Login response body is null");
        assertTrue(response.getBody().containsKey("token"), "Token not found in response body");

        log("4A test passed.");
    }

    @Test
    public void testGetMovieById() {
        log("[TEST] - 4B: Check [GET] /v1/movie/{id} endpoint ... ");
        String token = obtainAuthToken();

        ResponseEntity<Map> response = sendRequest("/movie/1", HttpMethod.GET, token, "", 200);
        assertNotNull(response.getBody(), "Response body is null");
        assertEquals(1, ((Number) response.getBody().get("movieId")).intValue(), "Expected movieId 1");
        assertEquals("Toy Story (1995)", response.getBody().get("title"), "Unexpected movie title");

        List<String> expectedGenres = List.of("Adventure", "Animation", "Children", "Comedy", "Fantasy");
        List<String> actualGenres = (List<String>) response.getBody().get("genres");
        assertNotNull(actualGenres, "Genres not found in response body");
        assertTrue(actualGenres.containsAll(expectedGenres), "Missing expected genres");

        log("[TEST] - 4B: Check [GET] /v1/movie/{id} endpoint with invalid id... ");
        response = sendRequest("/movie/999999", HttpMethod.GET, obtainAuthToken(), "", 404);
        assertEquals(404,  response.getStatusCode().value(), "Unexpected movieId");
        assertEquals("Movie not found", response.getBody().get("error"), "Unexpected movie error msg");
        log("4B test passed.");
    }

    @Test
    public void testGetRatingById() {
        log("[TEST] - 4C: Check [GET] /v1/rating/{id} endpoint ... ");
        ResponseEntity<Map> response = sendRequest("/rating/1", HttpMethod.GET, obtainAuthToken(), "", 200);
        assertNotNull(response.getBody(), "Response body is null");
        assertEquals(1, ((Number) response.getBody().get("movieId")).intValue(), "Expected movieId 1");
        assertNotNull(response.getBody().get("average_rating"), "\"average_rating\" not found in response body");
        log("[TEST] - 4C: Check [GET] /v1/rating/{id} endpoint with invalid id... ");
        response = sendRequest("/rating/999999", HttpMethod.GET, obtainAuthToken(), "", 404);
        assertEquals(404,  response.getStatusCode().value(), "Unexpected rating Id");
        assertEquals("No ratings found for this movie", response.getBody().get("error"), "Unexpected rate error msg");
        log("4C test passed.");
    }


    @Test
    public void testGetLinkById() {
        log("[TEST] - 4D: Check [GET] /v1/link/{id} endpoint ... ");
        ResponseEntity<Map> response = sendRequest("/link/1", HttpMethod.GET, obtainAuthToken(), "", 200);
        assertNotNull(response.getBody(), "Response body is null");
        assertEquals(1, ((Number) response.getBody().get("movieId")).intValue(), "Expected movieId 1");
        assertEquals("0114709", response.getBody().get("imdbId"), "Unexpected IMDb ID");
        assertEquals("862", response.getBody().get("tmdbId"), "Unexpected TMDb ID");
        log("[TEST] - 4D: Check [GET] /v1/link/{id} endpoint with invalid id... ");
        response = sendRequest("/link/999999", HttpMethod.GET, obtainAuthToken(), "", 404);
        assertEquals(404,  response.getStatusCode().value(), "Unexpected link Id");
        log("4D test passed.");
    }


    @Test
    public void testMovieEndpointsWithoutToken() {
        log("[TEST] - 4E: Check [GET] /v1/movie/{id} endpoint without token... ");
        sendRequest("/movie/1", HttpMethod.GET, "", "", 401);

        log("[TEST] - 4E: Check [GET] /v1/rating/{id} endpoint without token... ");
        sendRequest("/rating/1", HttpMethod.GET, "", "", 401);

        log("[TEST] - 4E: Check [GET] /v1/link/{id} endpoint without token... ");
        sendRequest("/link/1", HttpMethod.GET, "", "", 401);

        log("4E test passed.");
    }

    private String obtainAuthToken() {
        String email = "testuser" + System.currentTimeMillis() + "@example.com";
        String requestBody = String.format("{\"email\":\"%s\", \"password\":\"testpassword\"}", email);
        sendRequest("/register", HttpMethod.POST, "", requestBody, 201);
        ResponseEntity<Map> response = sendRequest("/login", HttpMethod.POST, "", requestBody, 200);

        return response.getBody().get("token").toString();
    }
}