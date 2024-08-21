package com.bangIt.blended.controller.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.bangIt.blended.common.security.CustomUserDetails;
import com.bangIt.blended.domain.dto.reservation.ReservationSaveDTO;
import com.bangIt.blended.service.user.ReservationService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RequiredArgsConstructor
@Controller
public class ReservationController {

	private final ReservationService service;

	//예약 등록
	@PostMapping("/reservationSave")
	public String placeSave(@AuthenticationPrincipal CustomUserDetails userDetails, ReservationSaveDTO dto) {
        Long reservationId = service.saveProcess(userDetails.getId(), dto);
        return "redirect:/payment?reservationId=" + reservationId;
    }
	
	//예약 조회
	@GetMapping("/reservation")
	public String getMethodName() {
		return "views/user/reservation/reservationList";
	}
	
}
