package com.bangIt.blended.service.partner;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.bangIt.blended.domain.dto.room.RoomListDTO;
import com.bangIt.blended.domain.dto.room.RoomSaveDTO;
import com.bangIt.blended.domain.enums.PlaceStatus;

public interface PartnerRoomService {

	void saveRoom(RoomSaveDTO dto);
	
	List<RoomListDTO> listProcess(Long placeId, PlaceStatus placeStatus);


	Map<String, String> uploadImage(MultipartFile file) throws IOException;

	void detailProcess(Long id, Model model);

}
