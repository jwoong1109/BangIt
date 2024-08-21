package com.bangIt.blended.common.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class OAuth2LoginController {

    // OAuth2 로그인 후 리다이렉트 처리 메서드
    @GetMapping("/oauth2/redirect")
    public String handleOAuth2Redirect(HttpSession session) {
        // 세션에서 "redirectToBusinessRegistration" 속성 값을 가져옴
        Boolean redirectToBusinessRegistration = (Boolean) session.getAttribute("redirectToBusinessRegistration");
        
        // "redirectToBusinessRegistration"이 true인 경우, 비즈니스 등록 페이지로 리다이렉트
        if (redirectToBusinessRegistration != null && redirectToBusinessRegistration) {
            session.removeAttribute("redirectToBusinessRegistration"); // 세션에서 속성을 제거
            return "redirect:/business-registration";
        }
        
        // 기본적으로 홈 페이지로 리다이렉트
        return "redirect:/";  
    }
}
