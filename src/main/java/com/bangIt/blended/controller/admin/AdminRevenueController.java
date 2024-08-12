package com.bangIt.blended.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class AdminRevenueController {

	
	@GetMapping("/admin/revenue")
	public String adminPartner() {
		return "/views/admin/revenue/list";
	}
	
	

}
