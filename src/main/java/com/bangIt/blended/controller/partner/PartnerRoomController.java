package com.bangIt.blended.controller.partner;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bangIt.blended.domain.dto.place.RoomSaveDTO;
import com.bangIt.blended.service.partner.PartnerRoomService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class PartnerRoomController {

	    private final PartnerRoomService roomService;

	    @GetMapping("/partner/roomRegisterForm")
	    public String roomRegisterForm(@RequestParam Long placeId, Model model) {
	        model.addAttribute("placeId", placeId);
	        return "views/partner/place/roomRegisterForm :: roomRegisterForm";
	    }

//	    @PostMapping("/partner/roomSave")
//	    @ResponseBody
//	    public ResponseEntity<?> roomSave(@ModelAttribute RoomSaveDTO roomSaveDTO) {
//	        try {
//	            roomService.saveRoom(roomSaveDTO);
//	            return ResponseEntity.ok().build();
//	        } catch (Exception e) {
//	            return ResponseEntity.badRequest().body(e.getMessage());
//	        }
//	    }

//	    @PostMapping("/roomSave")
//	    public String roomSave(RoomSaveDTO dto) {
//	        roomService.saveRoom(dto);
//	        return "redirect:/partner/placeList ";
//	    }
}
