package com.bangIt.blended.service.user;

import com.bangIt.blended.domain.dto.reservation.ReservationSaveDTO;

public interface ReservationService {

	Long saveProcess(Long id, ReservationSaveDTO dto);

	void updateReservationStatusToCompleted(Long reservationId);


}
