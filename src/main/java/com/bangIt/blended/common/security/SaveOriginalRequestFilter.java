package com.bangIt.blended.common.security;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SaveOriginalRequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        
        // /partner-login 경로를 요청할 때 원래 요청 경로를 세션에 저장
        if (uri.equals("/partner-login")) {
            request.getSession().setAttribute("originalRequest", uri);
        }

        filterChain.doFilter(request, response);
    }
}
