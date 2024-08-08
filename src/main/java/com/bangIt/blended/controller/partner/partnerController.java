package com.bangIt.blended.controller.partner;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class partnerController {
	
	@GetMapping("/partner")
	public String partner() {
		return "views/partner/partnerIndex";
	}
	
	//숙소등록
	@GetMapping("placeSave")
	public String placeSave() {
		return "views/partner/place/placeSave";
	}
	
	//숙소목록
	@GetMapping("placeList")
	public String placeList() {
		return "views/partner/place/placeList";
	}
	
	//숙소목록
	@GetMapping("reservation")
	public String reservation() {
		return "views/partner/reservation/reservation";
	}
	
	@GetMapping("/roomSave")
    public String roomSave() {
        return "/views/partner/room/roomSave"; // 동일한 템플릿을 반환합니다.
    }
	
}
