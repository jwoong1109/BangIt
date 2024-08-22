package com.bangIt.blended.domain.entity;

import java.time.Duration;
import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicUpdate;

import com.bangIt.blended.domain.dto.reservation.ReservationDetailDTO;
import com.bangIt.blended.domain.dto.reservation.ReservationListDTO;
import com.bangIt.blended.domain.enums.ReservationStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DynamicUpdate
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Table(name = "reservation")
public class ReservationEntity {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 예약 ID

    @Column(nullable = false)
    private LocalDateTime reservationDate;

    @Column(nullable = false)
    private LocalDateTime checkInDate;

    @Column(nullable = false)
    private LocalDateTime checkOutDate;

    @Column(nullable = false)
    private Long reservationPeople;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus reservationStatus;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private RoomEntity room;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    // 결제 정보와 1대1 관계, 외래 키를 사용
    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private PaymentEntity payment;

    // SaleEntity와 1대1 관계
    @OneToOne(mappedBy = "reservation")
    private SaleEntity sale;

 // 상태를 변경하는 메서드
    public void updateStatus(ReservationStatus newStatus) {
        this.reservationStatus = newStatus;
    }
    
    public ReservationListDTO toReservationListDTO() {
    	 Duration duration = Duration.between(checkInDate, checkOutDate);
         
         // Duration의 초를 일수로 변환
         long nights = duration.toDays();  // 소수점 이하 버림
    	
        return ReservationListDTO.builder()
            .id(id)
            .placeName(room.getPlace().getName())
            .roomName(room.getRoomName())
            .checkInDate(checkInDate)
            .checkOutDate(checkOutDate)
            .reservationPeople(reservationPeople)
            .totalPrice(room.getRoomPrice()*nights)
            .status(reservationStatus)
            .build();
    }

    private static final String BASE_URL = "https://s3.ap-northeast-2.amazonaws.com/nowon.images.host0521/";

    public ReservationDetailDTO toReservationDetailDTO() {
    	Duration duration = Duration.between(checkInDate, checkOutDate);
    	
    	String placeMainImageUrl = this.room.getPlace().getImages().stream()
                .filter(image -> image.getImageType() == ImageEntity.ImageType.PLACE_MAIN)
                .findFirst()
                .map(image -> BASE_URL + image.getImageUrl())
                .orElse(null);
        
        // Duration의 초를 일수로 변환
        long nights = duration.toDays();  // 소수점 이하 버림
        return ReservationDetailDTO.builder()
            .id(this.id)
            .placeName(this.room.getPlace().getName())
            .roomName(this.room.getRoomName())
            .checkInDate(this.checkInDate)
            .checkOutDate(this.checkOutDate)
            .reservationPeople(this.reservationPeople)
            .roomPrice(room.getRoomPrice())
            .totalPrice(room.getRoomPrice()*nights)
            .reservationStatus(this.reservationStatus)
            .nights(nights)
            .placeMainImageUrl(placeMainImageUrl)  
            // 필요한 다른 정보들 추가
            .build();
    }
}
