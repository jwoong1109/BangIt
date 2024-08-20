package com.bangIt.blended.domain.dto.user.admin;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetUserDtailDTO {
	
    private List<GetReservationDTO> reservations;
    private List<GetActivityLogDTO> activities;

}
