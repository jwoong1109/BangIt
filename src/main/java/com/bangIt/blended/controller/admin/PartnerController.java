package com.bangIt.blended.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class PartnerController {
	
	@GetMapping("/partner")
	public String list() {
		
		return "/views/admin/partner/partner";
	}
	
	
}
