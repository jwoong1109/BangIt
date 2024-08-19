package com.bangIt.blended.service.user;

import org.springframework.ui.Model;

public interface RoomService{

	void roomDetailProcess(long roomId, Model model);

	void listRoomsPlace(long placeId, Model model);

}
