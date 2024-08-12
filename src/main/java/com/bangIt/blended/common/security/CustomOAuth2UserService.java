package com.bangIt.blended.common.security;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.bangIt.blended.domain.entity.AuthProvider;
import com.bangIt.blended.domain.entity.Role;
import com.bangIt.blended.domain.entity.UserEntity;
import com.bangIt.blended.domain.repository.UserEntityRepository;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserEntityRepository repository;
    private final PasswordEncoder pw;
    private final HttpSession session;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        System.out.println("registrationID:" + registrationId);
		//구글인 경우, 네이버인 경우 다 다를 수 있음
		Map<String, Object> attributes = oAuth2User.getAttributes(); //Map으로 attribute를 제공하고 있음
		
		
		System.out.println(">>>>>");
		 for (String attributeName : attributes.keySet()) {
	            System.out.println(attributeName + ":" + attributes.get(attributeName));
	        }
		System.out.println("<<<<<");
		
		
		return socialUser(oAuth2User, registrationId);
    }

    private OAuth2User socialUser(OAuth2User oAuth2User, String registrationId) {
        String email = null;
        String name = null;
        String socialId = null; 
        AuthProvider provider = null;

        // 제공자별로 사용자 정보 추출
        if (registrationId.equals("google")) {
            email = oAuth2User.getAttribute("email");
            name = oAuth2User.getAttribute("name");
            socialId = oAuth2User.getAttribute("sub"); // 구글의 고유 ID는 String으로 반환됨
            provider = AuthProvider.GOOGLE;
        } else if (registrationId.equals("naver")) {
            Map<String, Object> response = oAuth2User.getAttribute("response");
            email = (String) response.get("email");
            name = (String) response.get("name");
            socialId = String.valueOf(response.get("id")); // 네이버의 고유 ID는 보통 String으로 반환됨
            provider = AuthProvider.NAVER;
        } else if (registrationId.equals("kakao")) {
            Map<String, Object> response = oAuth2User.getAttribute("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) response.get("profile");
            email = (String) response.get("email");
            name = (String) profile.get("nickname");
            socialId = String.valueOf(oAuth2User.getAttribute("id")); // 카카오의 고유 ID는 Long으로 반환될 수 있음, String으로 변환
            provider = AuthProvider.KAKAO;
        }

        Optional<UserEntity> existingUser = repository.findBySocialIdAndProvider(socialId, provider);

        if (existingUser.isEmpty()) {
            UserEntity newUser = UserEntity.builder()
                .email(email)
                .username(name)
                .password(pw.encode(String.valueOf(System.currentTimeMillis()))) // 임시 비밀번호 설정
                .socialId(socialId)
                .provider(provider)
                .build()
                .addRole(Role.USER);

            
            return new CustomUserDetails(repository.save(newUser));
        }

        return new CustomUserDetails(existingUser.get());
    }
}