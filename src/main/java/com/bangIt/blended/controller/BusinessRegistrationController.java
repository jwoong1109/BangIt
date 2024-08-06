package com.bangIt.blended.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BusinessRegistrationController {

	 @GetMapping("/business-registration")
	    public String showBusinessRegistrationPage() {
	        return "views/common/business-registration";
	    }
	
}
