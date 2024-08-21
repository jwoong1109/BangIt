package com.bangIt.blended.domain.dto.naverWorks;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetMyDriveDTO {
    private Long total;
    private Integer used;
    private Integer unused;
    private Integer trash;
    
	public static GetMyDriveDTO toDTO(JsonNode quota) {
		
		
		return GetMyDriveDTO.builder()
				            .total(quota.path("total").asLong())
				            .used(quota.path("used").asInt())
				            .unused(quota.path("unused").asInt())
				            .trash(quota.path("trash").asInt())		
							.build();
	}
    
    
   
}
