package com.bangIt.blended.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//config내부에서 메서드를 이용해서 메서드 빈으로 생성 / 빈을 만들지 않았으면 해당 페이지에 @컴포넌트로 진행 후 해당 변수를 쓰는 페이지에서 final 생성자 di 만들어주기
@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	
	//RequestCache : 사용자가 인증되지 않았던 페이지에 대한 정보를 저장하는 객체 //인증성공 후 저장된 요청을 복원하여 해당 페이지로 리다렉트
	private final RequestCache requestCache=new HttpSessionRequestCache();
	
	//RedirectStrategy: 리다렉트로 로직을 구현하는데 사용(처리객체)
	//인증, 인가 가정에서 특정 상황에 따라 사용자를 적절한 페이지로 리다렉트 하기위해 사용 
	//=> 로그인 성공(실패,로그아웃) 후 특정 페이지로 리다렉트 되도록 할때 쓰려고 한다.
	private final RedirectStrategy redirectStrategy=new DefaultRedirectStrategy();
	
	//private static final String DEFAULT_SUCCESS_URL="/"; //this.getDefaultTargetUrl();
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		//로그인페이지로 보내는 방법인 크게 2가지 존재 
		//1. 로그인 버튼을 눌러서 이동하는 방법
		//2. 로그인이 안된 상태에서 인증이 필요한기능을 요청하면 security에서 redirect 시켜 로그인 창으로 이동 
		//2번째의 경우 인증이 성공하면 최초 요청한 페이지로 이동 

		clearAuthenticationAttributes(request); //인증실패하거나 인증관련 메시지와 상태정보가 이후 요청에 영향을 미치지 않도록 제거
		String targetUrl=getDefaultTargetUrl();
		
		SavedRequest savedRequest=requestCache.getRequest(request, response);
		System.out.println("savedRequest: "+savedRequest);
		
		String prevPage=(String) request.getSession().getAttribute("prevPage");
		System.out.println("prevPage: "+prevPage);
		
		
		
		if(savedRequest != null && !savedRequest.getRedirectUrl().contains("login") ) {
			targetUrl=savedRequest.getRedirectUrl().split("[?]")[0];
			
		}else if(prevPage != null) {
			targetUrl=prevPage;
			request.getSession().removeAttribute("prevPage");
		}
		
		System.out.println("targetUrl: "+targetUrl);
		
		
		redirectStrategy.sendRedirect(request, response, targetUrl);
	
	}

}
