package com.bangIt.blended.domain.dto.naverWorks;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetMailDetailDTO {

	//메일
	
	private Integer mailId;
	private String fromName;
	private String fromEmail;
	private String toName;
	private String toEmail;
	private String subject;
	private String body;
	private String receivedTime;
	
	public static GetMailDetailDTO toDTO(JsonNode mailNode) {
		
		
		return GetMailDetailDTO.builder()
							.mailId(mailNode.path("mailId").asInt())	
							.subject(mailNode.path("subject").asText())	
							.body(mailNode.path("body").asText())	
							.receivedTime(mailNode.path("receivedTime").asText())		
							.fromName(mailNode.path("from").path("name").asText())	
							.fromEmail(mailNode.path("from").path("email").asText())
							.toName(mailNode.path("to").get(0).path("name").asText())
							.toEmail(mailNode.path("to").get(0).path("email").asText())
							.build();
	}

}
