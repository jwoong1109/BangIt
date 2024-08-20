package com.bangIt.blended.service.user.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bangIt.blended.domain.dto.reservation.ReservationSaveDTO;
import com.bangIt.blended.domain.entity.ReservationEntity;
import com.bangIt.blended.domain.entity.RoomEntity;
import com.bangIt.blended.domain.entity.UserEntity;
import com.bangIt.blended.domain.enums.ReservationStatus;
import com.bangIt.blended.domain.repository.ReservationEntityRepository;
import com.bangIt.blended.domain.repository.RoomEntityRepository;
import com.bangIt.blended.domain.repository.UserEntityRepository;
import com.bangIt.blended.service.user.ReservationService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ReservationServiceProcess implements ReservationService {

	private final ReservationEntityRepository reservationRepository;
	private final UserEntityRepository userRepository;
	private final RoomEntityRepository roomRepository;

	@Override
	@Transactional
	public Long saveProcess(Long userId, ReservationSaveDTO dto) {
		UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		RoomEntity room = roomRepository.findById(dto.getRoomId())
				.orElseThrow(() -> new RuntimeException("Room not found"));

		ReservationEntity reservation = ReservationEntity.builder().reservationDate(LocalDateTime.now())
				.checkInDate(dto.getCheckInDate()).checkOutDate(dto.getCheckOutDate())
				.reservationPeople(dto.getReservationPeople())
				.reservationStatus(
						dto.getReservationStatus() != null ? dto.getReservationStatus() : ReservationStatus.PENDING)
				.room(room).user(user).build();

		reservation = reservationRepository.save(reservation);

        return reservation.getId(); // 저장된 예약의 ID를 반환

		// 여기에 추가적인 로직을 구현할 수 있습니다.
		// 예: 방 가용성 업데이트, 알림 전송 등
	}
}
