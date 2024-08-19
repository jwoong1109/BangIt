package com.bangIt.blended.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class AdminDriveController {

	
	@GetMapping("/admin/drive")
	public String adminPartner() {
		return "/views/admin/drive/list";
	}
	
	

}
