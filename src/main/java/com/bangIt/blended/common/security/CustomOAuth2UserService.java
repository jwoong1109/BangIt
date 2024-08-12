package com.bangIt.blended.common.security;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.bangIt.blended.domain.entity.Role;
import com.bangIt.blended.domain.entity.UserEntity;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	
	private final PasswordEncoder pw;
	private final HttpSession session;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		
		// 인증 제공자 정보 가져오기
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		System.out.println("registrationID: " + registrationId);
		
		// 인증된 사용자의 속성 가져오기
		Map<String, Object> attributes = oAuth2User.getAttributes();
		System.out.println(">>>>>");
		for (String attributeName : attributes.keySet()) {
			System.out.println(attributeName + ":" + attributes.get(attributeName));
		}
		System.out.println("<<<<<");

		// OAuth 제공자 정보를 세션에 저장
		session.setAttribute("oauth_provider", registrationId);
		
		// 사용자 정보 처리
		return socialUser(oAuth2User, registrationId);
	}

	private OAuth2User socialUser(OAuth2User oAuth2User, String registrationId) {
		String email = null;
		String name = null;

		// 제공자별로 사용자 정보 추출
		if (registrationId.equals("google")) {
			email = oAuth2User.getAttribute("email");
			name = oAuth2User.getAttribute("name");
		} else if (registrationId.equals("naver")) {
			Map<String, Object> response = oAuth2User.getAttribute("response");
			email = (String) response.get("email");
			name = (String) response.get("name");
		} else if (registrationId.equals("kakao")) {
			Map<String, Object> response = oAuth2User.getAttribute("kakao_account");
			Map<String, Object> profile = (Map<String, Object>) response.get("profile");
			email = (String) response.get("email");
			name = (String) profile.get("nickname");
		}

		// 소셜 유저 정보를 엔티티로 변환
		UserEntity entity = UserEntity.builder()
				.email(email)
				.username(name)
				.password(pw.encode(String.valueOf(System.currentTimeMillis())))
				.build().addRole(Role.USER);
		
		return new CustomUserDetails(entity);
	}

}
