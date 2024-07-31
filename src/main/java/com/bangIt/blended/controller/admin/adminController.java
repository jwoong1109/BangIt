package com.bangIt.blended.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class adminController {
	
	@GetMapping("/admin")
	public String list() {
		
		
		return "views/admin/adminIndex";
	}
	

}
