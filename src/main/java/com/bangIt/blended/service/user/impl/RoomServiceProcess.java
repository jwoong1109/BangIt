package com.bangIt.blended.service.user.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.bangIt.blended.domain.entity.RoomEntity;
import com.bangIt.blended.domain.repository.RoomEntityRepository;
import com.bangIt.blended.service.user.RoomService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class RoomServiceProcess implements RoomService{

	private final RoomEntityRepository roomRepository;

	@Override
	public void roomInfoProcess(Long roomId, Model model) {
		Optional<RoomEntity> roomInfo= roomRepository.findById(roomId);
		model.addAttribute("roomInfo", roomInfo.stream().map(RoomEntity::toRoomDTO)
				.collect(Collectors.toList()));
		
	}

}
