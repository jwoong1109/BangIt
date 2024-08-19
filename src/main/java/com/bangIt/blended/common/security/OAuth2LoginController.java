package com.bangIt.blended.common.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class OAuth2LoginController {

    @GetMapping("/oauth2/redirect")
    public String handleOAuth2Redirect(HttpSession session) {
        Boolean redirectToBusinessRegistration = (Boolean) session.getAttribute("redirectToBusinessRegistration");
        
        if (redirectToBusinessRegistration != null && redirectToBusinessRegistration) {
            session.removeAttribute("redirectToBusinessRegistration");
            return "redirect:/business-registration";
        }
        
        return "redirect:/";  // 기본 리다이렉트 페이지
    }
}