package com.bangIt.blended.common.security;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.bangIt.blended.domain.entity.AuthProvider;
import com.bangIt.blended.domain.entity.Role;
import com.bangIt.blended.domain.entity.UserEntity;
import com.bangIt.blended.domain.repository.UserEntityRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserEntityRepository userRepository;

    public CustomAuthenticationSuccessHandler(UserEntityRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String targetUrl = "/";
        
        System.out.println("Current Request URI: " + request.getRequestURI());

        // 세션에서 원래 요청 URI 가져오기
        String originalRequestUri = (String) request.getSession().getAttribute("originalRequest");
        System.out.println("Original Request URI from session: " + originalRequestUri);

        if (originalRequestUri != null && originalRequestUri.equals("/partner-login")) {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

            String socialId = oAuth2User.getAttribute("sub");  // Google의 경우 'sub' 사용
            String providerName = "google"; // Google 로그인 시 고정 값 설정

            if (socialId == null || providerName == null) {
                throw new IllegalArgumentException("Social ID or provider is missing");
            }

            System.out.println("Social ID: " + socialId + ", Provider: " + providerName);

            AuthProvider provider = AuthProvider.valueOf(providerName.toUpperCase());
            Optional<UserEntity> userOptional = userRepository.findBySocialIdAndProvider(socialId, provider);

            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();
                boolean hasPartnerRole = user.getRoles().contains(Role.PARTNER);
                if (!hasPartnerRole) {
                    targetUrl = "/business-registration";
                }
            } else {
                throw new IllegalArgumentException("User not found for social ID: " + socialId + " and provider: " + providerName);
            }
        }

        System.out.println("Final Redirecting to URL: " + targetUrl);

        return targetUrl;
    }
}