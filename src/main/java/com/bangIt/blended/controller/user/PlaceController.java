package com.bangIt.blended.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class PlaceController {

	@GetMapping("/place")
	public String place() {
		return "views/user/place/place";
	}

	@GetMapping("/new")
	public String write() {
		return "views/partner/place/write";
	}

}
