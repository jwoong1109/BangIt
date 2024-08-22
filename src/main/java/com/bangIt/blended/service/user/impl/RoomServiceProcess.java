package com.bangIt.blended.service.user.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.bangIt.blended.domain.dto.room.RoomDetailDTO;
import com.bangIt.blended.domain.entity.ImageEntity;
import com.bangIt.blended.domain.entity.PlaceEntity;
import com.bangIt.blended.domain.entity.RoomEntity;
import com.bangIt.blended.domain.entity.ImageEntity.ImageType;
import com.bangIt.blended.domain.repository.PlaceEntityRepository;
import com.bangIt.blended.domain.repository.RoomEntityRepository;
import com.bangIt.blended.service.user.RoomService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class RoomServiceProcess implements RoomService {
    private final RoomEntityRepository roomRepository;

    private final PlaceEntityRepository placeRepository;
    private static final String BASE_URL = "https://s3.ap-northeast-2.amazonaws.com/nowon.images.host0521/";



    @Override
    @Transactional(readOnly = true)
    public void roomDetailProcess(long roomId, Model model) {
        RoomEntity room = roomRepository.findById(roomId)
            .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " + roomId));
        RoomDetailDTO roomDTO = convertToDTO(room);
        model.addAttribute("room", roomDTO);
    }


    @Override
    @Transactional(readOnly = true)
    public void listRoomsPlace(long placeId, Model model) {
        PlaceEntity place = placeRepository.findById(placeId)
            .orElseThrow(() -> new EntityNotFoundException("Place not found with id: " + placeId));
        List<RoomEntity> rooms = roomRepository.findByPlace(place);
        List<RoomDetailDTO> roomDTOs = rooms.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        model.addAttribute("rooms", roomDTOs);
    }

    private RoomDetailDTO convertToDTO(RoomEntity room) {
        String mainImage = null;
        List<String> additionalImages = new ArrayList<>();

        for (ImageEntity image : room.getImages()) {
            if(image.getImageUrl() == null || image.getImageUrl().isEmpty()) continue;

            String fullUrl = BASE_URL + image.getImageUrl();

            if (image.getImageType() == ImageType.ROOM_MAIN) {
                mainImage = fullUrl;
            } else if (image.getImageType() == ImageType.ROOM_ADDITIONAL) {
                additionalImages.add(fullUrl);
            }
        }

        return RoomDetailDTO.builder()
            .id(room.getId())
            .placeId(room.getPlace().getId())
            .roomName(room.getRoomName())
            .roomInformation(room.getRoomInformation())
            .roomPrice(room.getRoomPrice())
            .roomStatus(room.getRoomStatus().name())
            .refundPolicy(room.getRefundPolicy())
            .checkInTime(room.getCheckInTime())
            .checkOutTime(room.getCheckOutTime())
            .guests(room.getGuests())
            .mainImage(mainImage)
            .additionalImages(additionalImages)
            .build();
    }

	@Override
    public RoomDetailDTO getRoomDetailById(long roomId) {
        return roomRepository.findById(roomId)
            .map(RoomEntity::toRoomDetailDTO)
            .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " + roomId));
    }
}

