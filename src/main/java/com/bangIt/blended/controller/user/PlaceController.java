package com.bangIt.blended.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.bangIt.blended.service.user.UserPlaceService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class PlaceController {
	
	private final UserPlaceService service;

	@GetMapping("/place")
	public String place() {
		return "views/user/place/place";
	}
	
	//숙소 상세정보 조회
	@GetMapping("/place/detail/{no}")
	public String placeDetail(@PathVariable("no") long no, Model model) {
		service.detailProcess(no, model);
		
		return "views/user/place/place";
	}

}
