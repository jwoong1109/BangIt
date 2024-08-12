package com.bangIt.blended.common.security;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.bangIt.blended.domain.entity.UserEntity;

import lombok.Getter;

//소셜 로그인도 추가해버려
//다중상속은 불가하기 때문에 Oauth2에서 상속받았던 DefaultOAuth2UserService는 상속불가하기 때문에 인터페이스를 implements받음
@Getter //principal 객체에서 확인 가능 
public class CustomUserDetails extends User implements OAuth2User{

	private static final long serialVersionUID = 1L;
	
	//principal 에서 확인하기 위해 추가로 등록 가능(선택 사항)
	private String email; //username과 동일 
	private String name;
	
	private Map<String, Object> attributes;
	
	public CustomUserDetails(UserEntity entity){
		super(entity.getEmail(),entity.getPassword(),
				entity.getRoles().stream()
				.map(role->new SimpleGrantedAuthority("ROLE_"+role))
				.collect(Collectors.toSet()));
		
		
		email=entity.getEmail();
		name=entity.getUsername();
		
	
	}


	//추상메서드OAuth2User override
	@Override
	public Map<String, Object> getAttributes() {
		
		return attributes;
	}
	

}
