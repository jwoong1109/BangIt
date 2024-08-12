package com.bangIt.blended.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

    // RequestCache : 사용자가 인증되지 않았던 페이지에 대한 정보를 저장하는 객체
    private final RequestCache requestCache = new HttpSessionRequestCache();

    // RedirectStrategy : 리다이렉트 로직을 구현하는 데 사용
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // 인증 실패 시 메시지 제거
        clearAuthenticationAttributes(request);

        // 리다이렉션할 URL 결정
        String targetUrl = determineTargetUrl(request, response);
        logger.info("Redirecting to URL: {}", targetUrl);

        // 리다이렉션 수행
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        // 이전 요청을 복원하여 리다이렉션할 URL을 결정
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        String targetUrl = getDefaultTargetUrl();

        if (savedRequest != null && !savedRequest.getRedirectUrl().contains("login")) {
            // 저장된 요청이 있고 로그인 페이지가 아니라면 해당 URL로 리다이렉트
            targetUrl = savedRequest.getRedirectUrl().split("[?]")[0];
            logger.debug("Saved request found. Redirecting to saved URL: {}", targetUrl);
        } else {
            // 이전 페이지 URL이 세션에 저장되어 있는 경우
            String prevPage = (String) request.getSession().getAttribute("prevPage");
            if (prevPage != null) {
                targetUrl = prevPage;
                request.getSession().removeAttribute("prevPage");
                logger.debug("Previous page found in session. Redirecting to: {}", targetUrl);
            }
        }

        return targetUrl;
    }
}
