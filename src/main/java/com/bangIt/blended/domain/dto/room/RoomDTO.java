package com.bangIt.blended.domain.dto.room;

import java.time.LocalTime;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RoomDTO {
	private Long id;
	private String roomName;
	private Long roomPrice;
	private LocalTime checkInTime;
	private LocalTime checkOutTime;
	
}
