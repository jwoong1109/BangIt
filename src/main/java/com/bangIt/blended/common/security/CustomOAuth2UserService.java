package com.bangIt.blended.common.security;

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

        Map<String, Object> attributes = oAuth2User.getAttributes();
        System.out.println("registrationID: " + registrationId);
        attributes.forEach((key, value) -> System.out.println(key + ": " + value));

        return socialUser(oAuth2User, registrationId, attributes);
    }

    private OAuth2User socialUser(OAuth2User oAuth2User, String registrationId, Map<String, Object> attributes) {
        String email = null;
        String name = null;
        String socialId = null;
        AuthProvider provider = null;

        if (registrationId.equals("google")) {
            email = (String) attributes.getOrDefault("email", null);
            name = (String) attributes.getOrDefault("name", null);
            socialId = (String) attributes.getOrDefault("sub", null);
            provider = AuthProvider.GOOGLE;
        } else if (registrationId.equals("naver")) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            email = (String) response.getOrDefault("email", null);
            name = (String) response.getOrDefault("name", null);
            socialId = String.valueOf(response.getOrDefault("id", null));
            provider = AuthProvider.NAVER;
        } else if (registrationId.equals("kakao")) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) response.get("profile");
            email = (String) response.getOrDefault("email", null);
            name = (String) profile.getOrDefault("nickname", null);
            Long kakaoId = (Long) attributes.getOrDefault("id", null);
            socialId = String.valueOf(kakaoId);
            provider = AuthProvider.KAKAO;
        }

        System.out.println("Social ID: " + socialId);
        System.out.println("Provider: " + provider);

        Optional<UserEntity> existingUserOptional = repository.findBySocialIdAndProvider(socialId, provider);

        if (existingUserOptional.isEmpty()) {
            UserEntity newUser = UserEntity.builder()
                .email(email)
                .username(name)
                .password(pw.encode(String.valueOf(System.currentTimeMillis()))) // 임시 비밀번호 설정
                .socialId(socialId)
                .provider(provider)
                .build()
                .addRole(Role.USER);

            return new CustomUserDetails(repository.save(newUser), attributes);
        } else {
            UserEntity existingUser = existingUserOptional.get();

            // 현재 사용자의 역할을 확인
            if (existingUser.getRoles().contains(Role.USER) && !existingUser.getRoles().contains(Role.PARTNER)) {
                // 사용자에게 ROLE_USER만 있고 ROLE_PARTNER가 없는 경우
                if (request.getRequestURI().equals("/partner-login")) {
                    session.setAttribute("businessRegistrationUserId", existingUser.getId());
                }
            }

            return new CustomUserDetails(existingUser, attributes);
        }
    }
}
