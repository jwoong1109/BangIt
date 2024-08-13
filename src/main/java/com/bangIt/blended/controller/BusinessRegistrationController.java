package com.bangIt.blended.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.bangIt.blended.domain.dto.partner.PartnerSaveDTO;
import com.bangIt.blended.service.partner.BusinessRegistrationService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class BusinessRegistrationController {

	private final BusinessRegistrationService service;
	
	 @GetMapping("/business-registration")
	    public String showBusinessRegistrationPage() {
	        return "views/common/business-registration";
	    }
	 
		/*
		 * @PostMapping("/business-registration") public String
		 * registerBusiness(@RequestParam("businessNumber") String businessNumber,
		 * HttpSession session) { Long userId = (Long)
		 * session.getAttribute("businessRegistrationUserId"); if (userId != null) {
		 * service.registerBusiness(userId, businessNumber); } return "redirect:/"; }
		 */
	 
	 @PostMapping("/business-registration")
	 public String submitBusinessRegistration(PartnerSaveDTO dto) {
		 service.registerBusiness(dto);
	 	
	 	return "redirect:/";
	 }
	 
	 
	
}
