package com.bangIt.blended.domain.dto.naverWorks;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WritMailDTO {
	
	private String to;
	private String subject;
	private String body;
	private String contentType;
	private String userName;

}
