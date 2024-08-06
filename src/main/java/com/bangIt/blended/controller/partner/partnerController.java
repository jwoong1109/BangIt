package com.bangIt.blended.controller.partner;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class partnerController {
	
	@GetMapping("/partner")
	public String partner() {
		return "views/partner/partnerIndex";
	}
}
