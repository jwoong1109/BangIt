package com.bangIt.blended.domain.dto.reservation;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.bangIt.blended.domain.entity.ReservationEntity;
import com.bangIt.blended.domain.entity.RoomEntity;
import com.bangIt.blended.domain.entity.UserEntity;
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
public class ReservationSaveDTO {
	
	private Long roomId;
    private Long userId;  // 예약하는 사용자의 ID
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime checkInDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime checkOutDate;
    
    private Long reservationPeople;
    private ReservationStatus reservationStatus;  // 예약 상태 (PENDING 등)
    
    
    public ReservationEntity toReservationEntity(RoomEntity room, UserEntity user) {
        return ReservationEntity.builder()
            .reservationDate(LocalDateTime.now())
            .checkInDate(checkInDate)
            .checkOutDate(checkOutDate)
            .reservationPeople(reservationPeople)
            .reservationStatus(reservationStatus != null ? reservationStatus : ReservationStatus.PENDING)
            .room(room)
            .user(user)
            .build();
    }

}
