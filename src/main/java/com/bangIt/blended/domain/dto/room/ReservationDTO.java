package com.bangIt.blended.domain.dto.room;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.bangIt.blended.domain.entity.ImageEntity;
import com.bangIt.blended.domain.entity.ReservationEntity;
import com.bangIt.blended.domain.entity.RoomEntity;
import com.bangIt.blended.domain.entity.ImageEntity.ImageType;

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
    private String imageUrl; //이미지 URL
    private String roomName;  // 방 이름
    private Long roomPrice;  // 방 가격
    private String userName;  // 예약자 이름

    // ReservationEntity를 ReservationDTO로 변환하는 메서드
    public static ReservationDTO fromEntity(ReservationEntity reservation) {
        // 기본 이미지 URL 설정
        String baseUrl = "https://s3.ap-northeast-2.amazonaws.com/nowon.images.host0521/";
        String mainImageUrl = null;

        // RoomEntity에서 이미지 가져오기
        RoomEntity room = reservation.getRoom();
        for (ImageEntity image : room.getImages()) {
            if (image.getImageType() == ImageEntity.ImageType.ROOM_MAIN) {
                mainImageUrl = baseUrl + image.getImageUrl();
                break; // 첫 번째 ROOM_MAIN 이미지만 사용
            }
        }

        return ReservationDTO.builder()
                .id(reservation.getId())
                .reservationDate(reservation.getReservationDate())
                .checkInDate(reservation.getCheckInDate())
                .checkOutDate(reservation.getCheckOutDate())
                .reservationPeople(reservation.getReservationPeople())
                .imageUrl(mainImageUrl)
                .roomName(reservation.getRoom().getRoomName())
                .roomPrice(reservation.getRoom().getRoomPrice())
                .userName(reservation.getUser().getUsername())
                .build();
    }
}