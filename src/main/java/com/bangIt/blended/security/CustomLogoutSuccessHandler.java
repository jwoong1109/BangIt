package com.bangIt.blended.security;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        // 애플리케이션 내에서 로그아웃 후 리다이렉션할 경로 설정
        String redirectUrl = "/login";
        
        // 구글 로그아웃 URL
        String googleLogoutUrl = "https://accounts.google.com/Logout";
        
        // 현재 요청의 프로토콜, 호스트, 포트를 사용하여 리다이렉션 URL 구성
        String returnUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + redirectUrl;
        
        // URL 인코딩 적용
        String encodedReturnUrl = URLEncoder.encode(returnUrl, StandardCharsets.UTF_8.toString());

        // 구글 로그아웃 후 애플리케이션의 /login 경로로 리다이렉트
        response.sendRedirect(googleLogoutUrl + "?continue=" + encodedReturnUrl);
    }
}
