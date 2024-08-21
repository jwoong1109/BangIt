package com.bangIt.blended.domain.dto.user.admin;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GetActivityLogDTO {
	
    private Long id;
    private String activityType;
    private String description;
    private LocalDateTime timestamp;
    private String detailRecord;

}
