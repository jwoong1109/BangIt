package com.bangIt.blended.service.admin.impl;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.bangIt.blended.common.util.NaverWorksUtil;
import com.bangIt.blended.service.admin.AdminLoginService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AdminLoginServiceProcess implements AdminLoginService {
	
    private final NaverWorksUtil naverWorksUtil;
    
    
    //유틸에 정의해 조합된 getAuthorizationUrl을 response을 통해 get요청
	@Override
	public void getTokenProcess(HttpServletResponse response) throws IOException {
		String authorizationUrl = naverWorksUtil.getAuthorizationUrl();
		
		 response.sendRedirect(authorizationUrl);
		
	}

	
	//발급받은 토큰을 사용해 네이버api에서 제시한 억세스 토큰을 발급 받은 후 재사용하기 위해 세션에 저장
	@Override
	public void getAccessTokenProcess(String code, String state, HttpSession session) {
		try {
	        NaverWorksUtil.TokenResponse tokenResponse = naverWorksUtil.getAccessToken(code);
	        
	        if (tokenResponse != null) {
	        	
	        	session.setAttribute("accessToken", tokenResponse.getAccess_token());
	           
	        } else {
	            System.err.println("TokenResponse is null");
	        }
	    } catch (Exception e) {
	        System.err.println("Exception in getAccessTokenProcess: " + e.getMessage());
	    }
		
	}

}
