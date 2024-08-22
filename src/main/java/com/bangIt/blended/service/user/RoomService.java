package com.bangIt.blended.service.user;

import org.springframework.ui.Model;

import com.bangIt.blended.domain.dto.room.RoomDetailDTO;

public interface RoomService{

	void roomDetailProcess(long roomId, Model model);

	void listRoomsPlace(long placeId, Model model);

	RoomDetailDTO getRoomDetailById(long roomId);

}
