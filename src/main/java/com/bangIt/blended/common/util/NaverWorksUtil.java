package com.bangIt.blended.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.util.UUID;

@Component
public class NaverWorksUtil {

    //토큰요청을 위한 url
    private static final String AUTH_URL = "https://auth.worksmobile.com/oauth2/v2.0/authorize";
    private static final String TOKEN_URL = "https://auth.worksmobile.com/oauth2/v2.0/token";
    private static final String API_BASE_URL = "https://www.worksapis.com/v1.0";

    //토큰 요청에 필요한 정보들(프로퍼티즈에 정의되어 있음)
    @Value("${spring.naver.works.client.id}")
    private String clientId;

    @Value("${spring.naver.works.client.secret}")
    private String clientSecret;

    @Value("${spring.naver.works.client.redirectUri}")
    private String redirectUri;

    @Value("${spring.naver.works.client.scope}")
    private String scope;

    //
    private final RestTemplate restTemplate;

    //
    public NaverWorksUtil(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
            .setConnectTimeout(Duration.ofSeconds(10))
            .setReadTimeout(Duration.ofSeconds(10))
            .build();
    }

    //최종적으로 네이버가 요구하는 url 조합 생성
    public String getAuthorizationUrl() {
        String state = UUID.randomUUID().toString();
        return AUTH_URL + "?client_id=" + clientId +
               "&redirect_uri=" + redirectUri +
               "&scope=" + scope +
               "&response_type=code" +
               "&state=" + state;
    }

    //ResponseEntity를 사용해 post요청을 하기 위해 header와, body정보를 호환성이 맞는 map을 사용해 조합하여 토큰을 발급 받은 후
    //발급 받은 토큰을 TokenResponse에 저장
    public TokenResponse getAccessToken(String authorizationCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("code", authorizationCode);
        map.add("redirect_uri", redirectUri);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        try {
            ResponseEntity<TokenResponse> response = restTemplate.postForEntity(TOKEN_URL, request, TokenResponse.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Successfully retrieved access token");
                return response.getBody();
            } else {
                throw new RuntimeException("Failed to fetch token. Status code: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("Error fetching token: " + e.getMessage());
            throw new RuntimeException("Failed to fetch token", e);
        }
    }

    // 각 서비스에서 제네릭을 사용해 적용될 HTTP 요청 메서드
    public <T> ResponseEntity<T> get(String accessToken, String endpoint, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = API_BASE_URL + endpoint;
        return restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
    }

    public <T> ResponseEntity<T> post(String accessToken, String endpoint, Object body, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(body, headers);

        String url = API_BASE_URL + endpoint;
        return restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
    }

    // TokenResponse 클래스 정의
    @Setter
    @Getter
    public static class TokenResponse {
        private String access_token;
        private String token_type;
        private int expires_in;
        private String refresh_token;
        private String scope;
    }
}