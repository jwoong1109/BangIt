package com.bangIt.blended.domain.dto.naverWorks;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetEmployeeDTO {
	
    private String userId;
    private String email;
    private String fullName;
    private String cellPhone;
    private String hiredDate;
    private String employmentTypeName;
    private String birthdayCalendarType;
    private String levelName;
	
	
	public static GetEmployeeDTO toDTO(JsonNode user) {
		
		return GetEmployeeDTO.builder().userId(user.path("userID").asText())
									   .email(user.path("email").asText())
									   .fullName(user.path("userName").path("lastName").asText()+user.path("userName").path("firstName").asText())
									   .cellPhone(user.path("cellPhone").asText("미등록"))
									   .hiredDate(user.path("hiredDate").asText("미등록"))
									   .employmentTypeName(user.path("employmentTypeName").asText("미등록"))
									   .birthdayCalendarType(user.path("birthdayCalendarType").asText("미등록"))
									   .levelName(user.path("organizations").get(0).path("levelName").asText())
									   .build();
	}

}
