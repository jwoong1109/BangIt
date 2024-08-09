package com.bangIt.blended.controller.partner;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class PartnerController {
	
	@GetMapping("/partner")
	public String partner() {
		return "views/partner/partnerIndex";
	}
	
	//숙소등록 페이지이동
	@GetMapping("/partner/placeSave")
	public String placeSave() {
		return "views/partner/place/placeSave";
	}
	
	//숙소목록
	@GetMapping("partner/placeList")
	public String placeList() {
		return "views/partner/place/placeList";
	}
	
	//숙소목록
	@GetMapping("partner/reservation")
	public String reservation() {
		return "views/partner/reservation/reservation";
	}
	
	@GetMapping("partner/roomSave")
    public String roomSave() {
        return "/views/partner/room/roomSave"; // 동일한 템플릿을 반환합니다.
    }
	
}
