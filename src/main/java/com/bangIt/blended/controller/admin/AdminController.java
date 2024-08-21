package com.bangIt.blended.controller.admin;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bangIt.blended.service.admin.AdminLoginService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminController {
	
	private final AdminLoginService adminLoginService;

	
	//어드민 페이지에서 권한과 로그인을 확인하고 토근 발급 없다면,팝업 호출
	@GetMapping("/admin")
	public String list(Authentication authentication,Model model,HttpServletResponse response) throws IOException {
		
		if(authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) ) {
			
			adminLoginService.getTokenProcess(response);
			
			return null;
			
		}else {
			
			 model.addAttribute("errorMessage", "관리자 계정만 이용할 수 있는 사이트입니다.");
	         return "index"; 
			
		}
	}
	
	//발급된 인증토큰을 통해 억세스 토급 발급 후 인덱스 반환
    @GetMapping("/admin/oauth/callback")
    public String oauthCallback(@RequestParam(name = "code" ) String code, @RequestParam(name = "state") String state,HttpSession session) {
  
    	adminLoginService.getAccessTokenProcess(code,state,session);
    	
     
  
        return "views/admin/adminIndex"; // 인증 후 관리자 페이지로 리다이렉트
    }

	

	
}
