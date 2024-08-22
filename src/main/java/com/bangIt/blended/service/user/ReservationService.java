package com.bangIt.blended.service.user;

import org.springframework.ui.Model;

import com.bangIt.blended.domain.dto.reservation.ReservationSaveDTO;

public interface ReservationService {

	Long saveProcess(Long id, ReservationSaveDTO dto);

	void updateReservationStatusToCompleted(Long reservationId);

	void listProcess(Long id, Model model);

	void detailProcess(Long id, Model model);


}
