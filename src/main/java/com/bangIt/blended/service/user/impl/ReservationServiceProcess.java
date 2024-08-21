package com.bangIt.blended.service.user.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.bangIt.blended.domain.dto.reservation.ReservationListDTO;
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

	}


	@Override
	@Transactional
	public void updateReservationStatusToCompleted(Long reservationId) {
	    // 예약 ID를 기반으로 예약 엔티티를 조회
	    ReservationEntity reservation = reservationRepository.findById(reservationId)
	            .orElseThrow(() -> new RuntimeException("Reservation not found with ID: " + reservationId));

	    // 현재 예약 상태가 PENDING인 경우에만 COMPLETED로 변경
	    if (reservation.getReservationStatus() == ReservationStatus.PENDING) {
	        reservation.updateStatus(ReservationStatus.CONFIRMED);
	        reservationRepository.save(reservation);
	    } else {
	        // 상태 변경이 불가능한 경우 예외를 발생시킴
	        throw new IllegalStateException("Reservation cannot be updated to COMPLETED from status: " + reservation.getReservationStatus());
	    }
	}
	
	@Override
	public void listProcess(Long userId, Model model) {
	    UserEntity user = userRepository.findById(userId)
	        .orElseThrow(() -> new RuntimeException("User not found"));
	    
	    List<ReservationListDTO> reservationDTOs = reservationRepository.findByUser(user).stream()
	    	.map(ReservationEntity::toReservationListDTO)
	        .collect(Collectors.toList());

	    model.addAttribute("reservations", reservationDTOs);
	}

	@Override
    public void detailProcess(Long id, Model model) {
        ReservationEntity reservation = reservationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));
        
        model.addAttribute("reservation", reservation.toReservationDetailDTO());
    }

}
