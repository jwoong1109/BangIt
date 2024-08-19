package com.bangIt.blended.domain.dto.room;

import java.time.LocalTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoomSaveDTO {
	private Long placeId;
    private String roomName;
    private String roomInformation;
    private Long roomPrice;
    private String roomStatus;
    private String refundPolicy;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private Long guests;

    
    // 이미지 관련 필드 추가
    private String mainImageBucketKey;
    private String mainImageOrgName;
    private List<String> additionalImageBucketKeys;
    private List<String> additionalImageOrgNames;


}
