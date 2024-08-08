package com.bangIt.blended.security;

import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService; //원초적인 OAuth2 implement시 추가 설계를 해야함
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.bangIt.blended.domain.entity.Role;
import com.bangIt.blended.domain.entity.UserEntity;

import lombok.RequiredArgsConstructor;

//Custom userDetailsService와 같은 역할
//인증된걸 세션에 저장처리를 해야하기 때문에 만들어줌
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService{
	
	private final PasswordEncoder pw;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User=super.loadUser(userRequest);
		//원래는 로그인 만 가능하고 DB에 저장될 필요가 없는데, 간편 로그인/회원가입을 통해 DB에 저장할거임
		//원래는 로그인만 처리하고 회원가입은 후처리로 해야함, 미리 회원가입이 되어있는 사용자는 로그인만 되게 처리를 따로 해줘야함
		//naver or kakao or google or git 내가 인증정보를 가지고 왔으니깐 너가 DB를 가지말고 인증 처리를 해줘
		//구분해서 회원가입 처리를 해야한다
		
		//user요청정보
		//이걸로 어디서 로그인하고 있구나를 나눠서 정보를 얻어오기 위해 필요(google,kakao,naver에서 제공하는 인증정보가 다 다름)
		//인증정보를 제공하는 form이 달라요 .. 제공자 Provider를 구분하기 위해서 밑의 코드가 필요
		String registrationId=userRequest.getClientRegistration().getRegistrationId();
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
		String email=null;
		String name=null;
		if(registrationId.equals("google")) {
			email=oAuth2User.getAttribute("email");
			name=oAuth2User.getAttribute("name");
		}else if(registrationId.equals("naver")) {
			Map<String, Object> response=oAuth2User.getAttribute("response");
			email=(String) response.get("email");
			name=(String) response.get("name");
		}else if (registrationId.equals("kakao")) {
			// 카카오 로그인 정보 처리
			Map<String, Object> response=oAuth2User.getAttribute("kakao_account");
			Map<String, Object> profile=(Map<String, Object>) response.get("profile");
			email = (String) response.get("email");
			name = (String) response.get("nickname");
		}
		
		//회원가입되지 않고 소셜정보만 있는회원
		
		//소셜 정보가 멤버엔터티로 바뀌어서 소셜 유저로 포함이 됐습니다.
		UserEntity entity=UserEntity.builder()
				.email(email)
				.username(name)
				//소셜유저는 일반유저인증에서 제외해야함
				//소셜유저는 password가 의미없지만 임의의 password 값을 부여한 상황(DB에 password를 빈칸으로 둘수는 없으니깐)
				.password(pw.encode(String.valueOf(System.currentTimeMillis())))
				.build().addRole(Role.USER); //userRole 대체
		return new CustomUserDetails(entity);
	}
	


	
}
