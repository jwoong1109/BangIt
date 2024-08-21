package com.bangIt.blended.domain.dto.user;

import com.bangIt.blended.domain.enums.ReservationStatus;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReservationUpdateDTO {
	private Long reservationId;
    private ReservationStatus reservationStatus;
}
