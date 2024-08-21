package com.bangIt.blended.domain.dto.reservation;

import java.time.LocalDateTime;

import com.bangIt.blended.domain.enums.ReservationStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReservationDetailDTO {
	private Long id;
    private String placeName;
    private String roomName;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private Long reservationPeople;
    private Long roomPrice;
    private Long totalPrice;
    private ReservationStatus reservationStatus;
    private String imageUrl;
    private long nights;


}
