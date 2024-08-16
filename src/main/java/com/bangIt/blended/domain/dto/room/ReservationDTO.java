package com.bangIt.blended.domain.dto.room;

import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ReservationDTO {

	private Long id;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private LocalDateTime reservationDate;
    private int reservationPeople;
    private String reservationStatus;
    private String roomName;
    private Long roomPrice;
}
