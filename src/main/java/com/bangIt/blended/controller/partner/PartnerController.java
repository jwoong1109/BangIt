package com.bangIt.blended.controller.partner;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bangIt.blended.domain.dto.place.PlaceSaveDTO;
import com.bangIt.blended.service.partner.PartnerPlaceService;


import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Controller
public class PartnerController {
	
	private final PartnerPlaceService placeService;
	
	@GetMapping("/partner")
	public String partner() {
		return "views/partner/partnerIndex";
	}
	
	//숙소등록 페이지이동
	@GetMapping("/partner/placeNew")
	public String placeWrite() {
		return "views/partner/place/placeSave";
	}
	
	//숙소 등록하기
	@PostMapping("/partner/placeSave")
	public String placeSave(PlaceSaveDTO dto) {
		placeService.saveProcess(dto);
		return "redirect:/partner";
	}
	
	//숙소 및 방 이미지 저장
	@PostMapping("/partner/uploadImage")
    @ResponseBody
    public Map<String, String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        return placeService.s3TempUpload(file);
    }
	
	//숙소목록
	@GetMapping("partner/placeList")
	public String placeList() {
		return "views/partner/place/placeList";
	}
	
	//예약목록
	@GetMapping("partner/reservation")
	public String reservation() {
		return "views/partner/reservation/reservation";
	}
	
	@GetMapping("partner/roomSave")
    public String roomSave() {
        return "/views/partner/room/roomSave"; // 동일한 템플릿을 반환합니다.
    }
	
}
