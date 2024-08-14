package com.bangIt.blended.domain.dto.naverWorks;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetMailFolderDTO {
	
	private Integer folderId;
	private String folderType;
	private String folderName;
	private Integer unreadMailCount;
	private Integer mailCount;
	private Integer usage;
	
	
	public static GetMailFolderDTO toDTO(JsonNode mailFolders) {
		
		
		return GetMailFolderDTO.builder()
							   .folderId(mailFolders.path("folderId").asInt())
				               .folderType(mailFolders.path("folderType").asText())
				               .folderName(mailFolders.path("folderName").asText())
				               .unreadMailCount(mailFolders.path("unreadMailCount").asInt())
				               .mailCount(mailFolders.path("mailCount").asInt())
				               .usage(mailFolders.path("usage").asInt())
								.build();
	}

	

}
