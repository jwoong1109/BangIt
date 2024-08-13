package com.bangIt.blended.common.util;

import org.springframework.beans.factory.annotation.Value;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class NaverWorksUtil {
	
	
	//네이버 엑세스 토큰 발급시 사용
	@Value("${naver.works.client.id}")
	private static String clientId;
	
   @Value("${naver.works.client.secret}")
    private String clientSecret;

    @Value("${naver.works.client.redirect-uri}")
    private String redirectUri;

    @Value("${naver.works.client.scope}")
    private String scope;
    
    //관리자가 아닌 사용자에게 요청url 보호, 인증과 토큰 발급을 위한 url
    private static final String AUTH_URL = "https://auth.worksmobile.com/oauth2/v2.0/authorize";
    private static final String TOKEN_URL = "https://auth.worksmobile.com/oauth2/v2.0/token";
    
    
    
    

	
	

}
