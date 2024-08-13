package com.bangIt.blended.controller;

import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bangIt.blended.service.partner.BusinessRegistrationService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BusinessRegistrationController {

    private final BusinessRegistrationService businessRegistrationService;

    @GetMapping("/business-registration")
    public String showBusinessRegistrationForm() {
        return "views/common/business-registration"; // 뷰 파일 이름
    }

    @PostMapping("/business-registration")
    public String registerBusiness(@RequestParam("businessNumber") long businessNumber, 
                                   @AuthenticationPrincipal OAuth2User oauth2User) {
        // registrationId를 사용자 속성에서 가져옵니다.
        String registrationId = oauth2User.getAttribute("registrationId");
        String socialId = null;

        if ("google".equals(registrationId)) {
            socialId = oauth2User.getAttribute("sub");
        } else if ("naver".equals(registrationId)) {
            socialId = oauth2User.getAttribute("id");
        } else if ("kakao".equals(registrationId)) {
            Long kakaoId = oauth2User.getAttribute("id"); // 카카오는 보통 Long 타입으로 반환됩니다.
            socialId = kakaoId != null ? String.valueOf(kakaoId) : null;
        }

        // 비즈니스 로직을 서비스 레이어로 위임
        businessRegistrationService.registerBusiness(businessNumber, socialId, registrationId);

        return "redirect:/"; // 성공 시 리디렉션할 페이지
    }

}