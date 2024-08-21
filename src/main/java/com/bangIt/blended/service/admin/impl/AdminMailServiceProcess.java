package com.bangIt.blended.service.admin.impl;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.bangIt.blended.common.util.NaverWorksUtil;
import com.bangIt.blended.domain.dto.naverWorks.GetMailDetailAttachmentFileDTO;
import com.bangIt.blended.domain.dto.naverWorks.GetMailDetailDTO;
import com.bangIt.blended.domain.dto.naverWorks.GetMailFolderDTO;
import com.bangIt.blended.domain.dto.naverWorks.GetMailListDTO;
import com.bangIt.blended.domain.dto.naverWorks.WritMailDTO;
import com.bangIt.blended.service.admin.AdminMailService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdminMailServiceProcess implements AdminMailService {

	private final NaverWorksUtil naverWorksUtil;

	// 메일함 정보 가져오기
	@Override
	public void getMailFolderProcess(Model model, String accessToken) {

		try {

			ResponseEntity<JsonNode> response = naverWorksUtil.get(accessToken, "/users/me/mail/mailfolders");
			 JsonNode responseBody = response.getBody();
	         System.out.println("API Response: " + responseBody);
	         
	        if(response.getStatusCode() == HttpStatus.OK) {
	        	model.addAttribute("mailFolder", StreamSupport.stream(response.getBody().path("mailFolders").spliterator(),false).map(GetMailFolderDTO::toDTO).collect(Collectors.toList()));
	        	
	        }

		} catch (Exception e) {
			model.addAttribute("errorMessage", "Error fetching Mail list: " + e.getMessage());
		}

	}
	
	
   //특정 메일함의 목록 정보 가져오기 
	@Override
	public void getMailFolderListProcess(String folderId, Model model, String accessToken) {
		
		try {
			ResponseEntity<JsonNode> response = naverWorksUtil.get(accessToken, String.format("/users/me/mail/mailfolders/%s/children", folderId));
			 JsonNode responseBody = response.getBody();
	         System.out.println("API Response: " + responseBody);
			
			 if(response.getStatusCode() == HttpStatus.OK) {
				 
				 model.addAttribute("mailList", StreamSupport.stream(response.getBody().path("mails").spliterator(),false).map(GetMailListDTO::toDTO).collect(Collectors.toList()));
		        	
		        	
		        }
		} catch (Exception e) {
			model.addAttribute("errorMessage", "Error fetching Mail list: " + e.getMessage());
		}
		
	}

    //특정 메일 내용 가져오기
	@Override
	public void getMailDetail(String mailId, Model model, String accessToken) {

		try {
			ResponseEntity<JsonNode> response = naverWorksUtil.get(accessToken, String.format("/users/me/mail/%s", mailId));
			 JsonNode responseBody = response.getBody();
	         System.out.println("API Response: " + responseBody);
			
			 if(response.getStatusCode() == HttpStatus.OK) {
				 
				 model.addAttribute("mailDetail", GetMailDetailDTO.toDTO(response.getBody().path("mail")));
				 model.addAttribute("mailDetailAttachmentFile", StreamSupport.stream(response.getBody().path("attachments").spliterator(),false).map(GetMailDetailAttachmentFileDTO::toDTO).collect(Collectors.toList()));
		        	
		        	
		        }
		} catch (Exception e) {
			model.addAttribute("errorMessage", "Error fetching Mail list: " + e.getMessage());
		}
		
	}

    //메일 보내기
	@Override
	public void WritMailProcess(WritMailDTO dto, String accessToken) {
		
		  ObjectMapper objectMapper = new ObjectMapper();
		    ObjectNode requestBody = objectMapper.createObjectNode();

		    requestBody.put("to", dto.getTo());  // 문자열로 설정
		    requestBody.put("subject", dto.getSubject());
		    requestBody.put("body", dto.getBody());
		    requestBody.put("contentType", dto.getContentType());
		    requestBody.put("userName", dto.getUserName());
		    
		    // 기본값 설정 (필요에 따라 DTO에서 가져오도록 수정 가능)
		    requestBody.put("isSaveSetMail", true);
		    requestBody.put("isSaveTracking", true);
		    requestBody.put("isSendSeparately", false);

			ResponseEntity<JsonNode> response = naverWorksUtil.post(accessToken,"/users/me/mail",requestBody);
			
			if (response.getStatusCode().is2xxSuccessful()) {
		        System.out.println("메일 전송 성공: " + response.getBody());
		    } else {
		        System.out.println("메일 전송 실패: " + response.getStatusCode() + " - " + response.getBody());
		    }
		
			

	       }
		
	

	
	
	
	
	
	
	
	
}
