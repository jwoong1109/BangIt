package com.bangIt.blended.domain.dto.reservation;

import java.time.LocalDateTime;

import com.bangIt.blended.domain.enums.ReservationStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationListDTO {
	
	private Long id;
    private String placeName;
    private String roomName;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private Long reservationPeople;
    private Long totalPrice;
    private ReservationStatus status;
}
