package com.bangIt.blended.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class PlaceListController {
	
	
	@GetMapping("/placeslist")
	public String getMethodName() {
		return "/views/user/placeList/placelist";
	}
	

}
