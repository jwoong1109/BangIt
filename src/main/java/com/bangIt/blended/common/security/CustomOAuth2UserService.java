package com.bangIt.blended.common.security;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.bangIt.blended.domain.entity.AuthProvider;
import com.bangIt.blended.domain.entity.Role;
import com.bangIt.blended.domain.entity.UserEntity;
import com.bangIt.blended.domain.repository.PartnerEntityRepository;
import com.bangIt.blended.domain.repository.UserEntityRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserEntityRepository repository;
    private final PartnerEntityRepository partnerRepository;
    private final PasswordEncoder pw;
    private final HttpSession session;
    private final HttpServletRequest request;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // attributes를 수정 가능한 맵으로 복사합니다.
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        attributes.put("registrationId", registrationId);

        System.out.println("registrationID: " + registrationId);
        attributes.forEach((key, value) -> System.out.println(key + ": " + value));

        return socialUser(oAuth2User, registrationId, attributes);
    }



    private OAuth2User socialUser(OAuth2User oAuth2User, String registrationId, Map<String, Object> attributes) {
        String email = null;
        String socialId = null;
        AuthProvider provider = null;

        if (registrationId.equals("google")) {
            email = (String) attributes.getOrDefault("email", null);
            socialId = (String) attributes.getOrDefault("sub", null);
            provider = AuthProvider.GOOGLE;
        } else if (registrationId.equals("naver")) {
            // Naver의 응답 데이터를 파싱
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            if (response != null) {
                email = (String) response.getOrDefault("email", null);
                socialId = String.valueOf(response.getOrDefault("id", null));
            }
            provider = AuthProvider.NAVER;
        } else if (registrationId.equals("kakao")) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");
            email = (String) response.getOrDefault("email", null);
            Long kakaoId = (Long) attributes.getOrDefault("id", null);
            socialId = String.valueOf(kakaoId);
            provider = AuthProvider.KAKAO;
        }

        System.out.println("Social ID: " + socialId);
        System.out.println("Provider: " + provider);

        // socialId가 null인지 확인하고 예외를 던짐
        if (socialId == null || socialId.isEmpty()) {
            throw new IllegalArgumentException("Failed to retrieve social ID from " + provider);
        }

        Optional<UserEntity> existingUserOptional = repository.findBySocialIdAndProvider(socialId, provider);

        if (existingUserOptional.isEmpty()) {
            UserEntity newUser = UserEntity.builder()
                .email(email)
                .password(pw.encode(String.valueOf(System.currentTimeMillis())))
                .socialId(socialId)
                .provider(provider)
                .build()
                .addRole(Role.USER);

            return new CustomUserDetails(repository.save(newUser), attributes);
        } else {
            UserEntity existingUser = existingUserOptional.get();

            // 현재 요청 URI가 /partner-login이고, 사용자가 ROLE_USER만 가지고 있는 경우
            if (request.getRequestURI().equals("/partner-login") && 
                existingUser.getRoles().contains(Role.USER) && 
                !existingUser.getRoles().contains(Role.PARTNER)) {
                
                // 세션에 사용자 ID 저장 (business-registration 페이지에서 사용)
                session.setAttribute("businessRegistrationUserId", existingUser.getId());
                
                // business-registration 페이지로 리다이렉트하기 위한 플래그 설정
                session.setAttribute("redirectToBusinessRegistration", true);
            }

            return new CustomUserDetails(existingUser, attributes);
        }
    }
}