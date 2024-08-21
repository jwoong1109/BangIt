package com.bangIt.blended.domain.dto.naverWorks;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetMailDetailAttachmentFileDTO {
	
	//첨부파일
	private Integer attachmentId;
	private String contentType;
	private String charset;
	private String filename;
	private String encoding;
	private String size;
	private String bigFileExpireTime;
	private String bigFileFid;
	private String cid;
	private String contentDisposition;
	private String data	;
	
	
	public static GetMailDetailAttachmentFileDTO toDTO(JsonNode attachmentFileNode) {
		
		
		return GetMailDetailAttachmentFileDTO.builder()
							.attachmentId(attachmentFileNode.path("attachmentId").asInt())	
							.contentType(attachmentFileNode.path("contentType").asText())	
							.charset(attachmentFileNode.path("charset").asText())	
							.filename(attachmentFileNode.path("filename").asText())	
							.encoding(attachmentFileNode.path("encoding").asText())
							.size((attachmentFileNode.path("size").asInt() / 1024) + "KB")	
							.bigFileExpireTime(attachmentFileNode.path("bigFileExpireTime").asText())	
							.bigFileFid(attachmentFileNode.path("bigFileFid").asText())
							.cid(attachmentFileNode.path("cid").asText())
							.contentDisposition(attachmentFileNode.path("contentDisposition").asText())
							.data(attachmentFileNode.path("data").asText())
							.build();
	}

}
