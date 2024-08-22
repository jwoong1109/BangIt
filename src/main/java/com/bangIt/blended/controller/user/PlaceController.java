package com.bangIt.blended.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bangIt.blended.domain.dto.room.RoomDetailDTO;
import com.bangIt.blended.service.user.RoomService;
import com.bangIt.blended.service.user.UserPlaceService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class PlaceController {
	
	private final UserPlaceService service;
	private final RoomService roomService;

	@GetMapping("/place")
	public String listPlaces() {
		return "views/user/place/placeList";
	}
	
	// 숙소 상세정보 조회 (방 목록 포함)
    @GetMapping("/place/detail/{placeId}")
    public String placeDetail(@PathVariable("placeId") long placeId, Model model) {
        service.detailProcess(placeId, model);
        roomService.listRoomsPlace(placeId, model);
        return "views/user/place/placeDetail";
    }
    
    // 방 상세정보 조회 (AJAX 요청 처리)
    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<RoomDetailDTO> getRoomDetails(@PathVariable long roomId) {
        return ResponseEntity.ok(roomService.getRoomDetailById(roomId));
    }
	

}
