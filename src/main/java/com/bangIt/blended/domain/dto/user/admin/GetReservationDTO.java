package com.bangIt.blended.domain.dto.user.admin;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.bangIt.blended.domain.enums.ReservationStatus;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GetReservationDTO {
	
    private Long id;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private LocalDateTime reservationDate;
    private int reservationPeople;
    private ReservationStatus reservationStatus;
    private String roomName;
    private String placeName;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private LocalDateTime paymentDate;
    private String paymentMethod;
}
