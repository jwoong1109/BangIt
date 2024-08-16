package com.bangIt.blended.service.user.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.bangIt.blended.domain.entity.ReservationEntity;
import com.bangIt.blended.domain.entity.RoomEntity;
import com.bangIt.blended.domain.repository.ReservationEntityRepository;
import com.bangIt.blended.domain.repository.RoomEntityRepository;
import com.bangIt.blended.service.user.ReservationService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ReservationServiceProcess implements ReservationService {
	
	private final ReservationEntityRepository reservationRepository;
	private final RoomEntityRepository roomRepository;
	
	@Override
	public void getReservationInfo(Long reservationId,Long roomId, Model model) {
		
		 Optional<ReservationEntity> reservedInfo = reservationRepository.findById(reservationId);
		 model.addAttribute("reservedInfo", reservedInfo.stream().map(ReservationEntity::toReservationDTO)
					.collect(Collectors.toList()));
	}

}
