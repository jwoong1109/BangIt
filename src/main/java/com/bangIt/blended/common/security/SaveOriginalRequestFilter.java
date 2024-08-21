package com.bangIt.blended.common.security;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SaveOriginalRequestFilter extends OncePerRequestFilter {

    // 요청이 필터를 통과할 때마다 한 번씩 실행되는 메서드
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // 현재 요청의 URI를 가져옴
        String uri = request.getRequestURI();
        
        // 요청 URI가 "/partner-login"인 경우
        if (uri.equals("/partner-login")) {
            // 세션에 "originalRequest"라는 이름으로 URI를 저장
            request.getSession().setAttribute("originalRequest", uri);
        }

        // 다음 필터로 요청과 응답을 전달하여 체인을 이어감
        filterChain.doFilter(request, response);
    }
}
