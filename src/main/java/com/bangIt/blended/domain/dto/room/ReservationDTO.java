package com.bangIt.blended.domain.dto.room;

import java.time.LocalDateTime;

import com.bangIt.blended.domain.entity.ReservationEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

    private Long id;  // 예약 ID
    private LocalDateTime reservationDate;  // 예약일
    private LocalDateTime checkInDate;  // 체크인 날짜
    private LocalDateTime checkOutDate;  // 체크아웃 날짜
    private Long reservationPeople;  // 예약 인원
    private String roomName;  // 방 이름
    private Long roomPrice;  // 방 가격
    private String userName;  // 예약자 이름

    // ReservationEntity를 ReservationDTO로 변환하는 메서드
    public static ReservationDTO fromEntity(ReservationEntity reservation) {
        return ReservationDTO.builder()
                .id(reservation.getId())
                .reservationDate(reservation.getReservationDate())
                .checkInDate(reservation.getCheckInDate())
                .checkOutDate(reservation.getCheckOutDate())
                .reservationPeople(reservation.getReservationPeople())
                .roomName(reservation.getRoom().getRoomName())
                .roomPrice(reservation.getRoom().getRoomPrice())
                .userName(reservation.getUser().getUsername())
                .build();
    }
}