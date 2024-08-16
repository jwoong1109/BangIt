package com.bangIt.blended.controller.partner;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.bangIt.blended.domain.dto.place.RoomSaveDTO;
import com.bangIt.blended.service.partner.PartnerRoomService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class PartnerRoomController {

	    private final PartnerRoomService roomService;

//	    @GetMapping("/roomSave")
//	    public String roomSaveForm(Model model) {
//	        model.addAttribute("places", roomService.getAllPlaces());
//	        return "views/partner/room/roomSave";
//	    }

	    @PostMapping("/roomSave")
	    public String roomSave(RoomSaveDTO dto) {
	        roomService.saveRoom(dto);
	        return "redirect:/partner/placeList";
	    }
}
