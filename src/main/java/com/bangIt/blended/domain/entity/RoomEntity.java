package com.bangIt.blended.domain.entity;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.annotations.DynamicUpdate;

import com.bangIt.blended.domain.dto.room.RoomDetailDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "room")
@Getter
@Entity
public class RoomEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne
	@JoinColumn(name = "place_id", nullable = false)
	private PlaceEntity place;

	@Column(nullable = false)
	private String roomName;

	@Column(columnDefinition = "TEXT")
	private String roomInformation;

	@Column(nullable = false)
	private Long roomPrice;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private RoomStatus roomStatus;

	@Column(nullable = false)
	private String refundPolicy;

	@Column(nullable = false)
	private LocalTime checkInTime;

	@Column(nullable = false)
	private LocalTime checkOutTime;
	
	@Column(nullable = false)
	private Long guests;
	
	@OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ImageEntity> images;

	// RoomStatus enum (방 상태를 나타내는 열거형)
	@Getter
	@RequiredArgsConstructor
	public enum RoomStatus {
		AVAILABLE(1, "이용 가능"), 
		BOOKED(2, "예약됨"), 
		MAINTENANCE(3, "유지보수 중");

		private final int number;
		private final String KoName;
	}

	public RoomDetailDTO toRoomDetailDTO() {
	    String baseUrl = "https://s3.ap-northeast-2.amazonaws.com/nowon.images.host0521/";
	    String mainImage = null;
	    List<String> additionalImages = null;

	    if (images != null && !images.isEmpty()) {
	        mainImage = images.stream()
	            .filter(img -> img.getImageType() == ImageEntity.ImageType.ROOM_MAIN)
	            .findFirst()
	            .map(img -> baseUrl + img.getImageUrl())
	            .orElse(null);

	        additionalImages = images.stream()
	            .filter(img -> img.getImageType() == ImageEntity.ImageType.ROOM_ADDITIONAL)
	            .map(img -> baseUrl + img.getImageUrl())
	            .collect(Collectors.toList());
	    }

	    return RoomDetailDTO.builder()
	        .placeId(this.place != null ? this.place.getId() : null)
	        .id(this.id)
	        .roomName(this.roomName)
	        .roomInformation(this.roomInformation)
	        .roomPrice(this.roomPrice)
	        .roomStatus(this.roomStatus != null ? this.roomStatus.getKoName() : null)
	        .refundPolicy(this.refundPolicy)
	        .checkInTime(this.checkInTime)
	        .checkOutTime(this.checkOutTime)
	        .guests(this.guests)
	        .mainImage(mainImage)
	        .additionalImages(additionalImages)
	        .build();
	}
}
