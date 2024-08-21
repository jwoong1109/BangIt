package com.bangIt.blended.domain.dto.naverWorks;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetMyDriveFilesDTO {
    private String accessedTime;
    private String createdTime;
    private String fileId;
    private String parentFileId;
    private String fileName;
    private Integer fileSize;
    private String filePath;
    private String fileType;

	public static GetMyDriveFilesDTO toDTO(JsonNode files) {
		
		
		return GetMyDriveFilesDTO.builder()
				            .accessedTime(files.path("accessedTime").asText())
				            .createdTime(files.path("createdTime").asText())
				            .fileId(files.path("fileId").asText())
				            .parentFileId(files.path("parentFileId").asText())
				            .fileName(files.path("fileName").asText())
				            .fileSize(files.path("fileSize").asInt())
				            .filePath(files.path("filePath").asText())
				            .fileType(files.path("fileType").asText())
							.build();
	}
    
    
   
}
