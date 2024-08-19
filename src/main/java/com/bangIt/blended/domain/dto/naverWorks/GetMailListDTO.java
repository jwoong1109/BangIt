package com.bangIt.blended.domain.dto.naverWorks;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetMailListDTO {
	
	private Integer mailId;
	private String status;
	private String subject;
	private String receivedTime;
	private String size;
	private Integer attachCount;
	private String formName;
	private String formEmail;
	
	
	
	public static GetMailListDTO toDTO(JsonNode mailNode) {
		
		
		return GetMailListDTO.builder()
							.mailId(mailNode.path("mailId").asInt())	
							.status(mailNode.path("status").asText())	
							.subject(mailNode.path("subject").asText())	
							.receivedTime( OffsetDateTime.parse(mailNode.path("receivedTime").asText()).format(DateTimeFormatter.ISO_LOCAL_DATE))	
							.size((mailNode.path("size").asInt() / 1024) + "KB")	
							.attachCount(mailNode.path("attachCount").asInt())	
							.formName(mailNode.path("from").path("name").asText())	
							.formEmail(mailNode.path("from").path("email").asText())
							.build();
	}

}
