package com.bangIt.blended.service.admin.impl;

import java.io.IOException;
import java.time.Instant;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.bangIt.blended.common.util.NaverWorksUtil;
import com.bangIt.blended.domain.dto.naverWorks.GetMailFolderDTO;
import com.bangIt.blended.domain.dto.naverWorks.GetMyDriveDTO;
import com.bangIt.blended.domain.dto.naverWorks.GetMyDriveFilesDTO;
import com.bangIt.blended.service.admin.AdminDriveService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class AdminDriveServiceProcess implements AdminDriveService {
	
	private final NaverWorksUtil naverWorksUtil;

	//내드라이브 속성 조회
	@Override
	public void getMyDriveProcess(Model model, String accessToken) {
		
		try {

			ResponseEntity<JsonNode> response = naverWorksUtil.get(accessToken, "/users/me/drive");
			 JsonNode responseBody = response.getBody();
	         System.out.println("API Response: " + responseBody);
	         
	        if(response.getStatusCode() == HttpStatus.OK) {
	        	model.addAttribute("myDrive",GetMyDriveDTO.toDTO(responseBody.path("quota")));
	        	
	        }

		} catch (Exception e) {
			model.addAttribute("errorMessage", "Error fetching Mail list: " + e.getMessage());
		}
		
	}
     
	//내 드라이브 루트파일 조회
	@Override
	public void getMyDriveRootFileProcess(Model model, String accessToken) {
		
		try {

			ResponseEntity<JsonNode> response = naverWorksUtil.get(accessToken, "/users/me/drive/files");
			 JsonNode responseBody = response.getBody();
	         System.out.println("API Response: " + responseBody);
	         
	        if(response.getStatusCode() == HttpStatus.OK) {
	        	model.addAttribute("myDriveFiles", StreamSupport.stream(response.getBody().path("files").spliterator(),false).map(GetMyDriveFilesDTO::toDTO).collect(Collectors.toList()));
	        	
	        }

		} catch (Exception e) {
			model.addAttribute("errorMessage", "Error fetching Mail list: " + e.getMessage());
		}
	
		
	}

	//특정 파일 다운로드
	@Override
	public ResponseEntity<ByteArrayResource> getFildeDownloadUrl(String accessToken, String fileId) {
		
		 try {
		        String url = String.format("/users/me/drive/files/%s/download", fileId);
		        ResponseEntity<byte[]> response = naverWorksUtil.getFile(accessToken, url);
		        System.out.println("API Response Headers: " + response.getHeaders());
		        
		        if (response.getStatusCode().is2xxSuccessful()) {
		            ByteArrayResource resource = new ByteArrayResource(response.getBody());
		            
		            HttpHeaders headers = new HttpHeaders();
		            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileId);
		            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		            
		            return ResponseEntity.ok()
		                .headers(headers)
		                .contentLength(resource.contentLength())
		                .body(resource);
		        } else {
		            return ResponseEntity.status(response.getStatusCode()).build();
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		        return ResponseEntity.internalServerError().build();
		    }
		
	}

	//특정 파일 삭제 
	@Override
	public void deleteFilde(String accessToken, String fileId) {
		
		try {

			ResponseEntity<JsonNode> response = naverWorksUtil.delete(accessToken, String.format("/users/me/drive/files/%s", fileId));
			 JsonNode responseBody = response.getBody();
	         System.out.println("API Response: " + responseBody);
	         
	        if(response.getStatusCode() == HttpStatus.OK) {
	        	
	        	
	        }

		} catch (Exception e) {
			
		}
		
		
	}

	//파일 업로드
	@Override
	public void postFile(MultipartFile file,String accessToken) throws IOException {
	   ObjectMapper objectMapper = new ObjectMapper();
	   ObjectNode fileMetadata = objectMapper.createObjectNode();
	   
        fileMetadata.put("fileName", file.getOriginalFilename());
        fileMetadata.put("modifiedTime", Instant.now().toString());
        fileMetadata.put("fileSize", file.getSize());
        fileMetadata.put("overwrite", false);
        
        ResponseEntity<JsonNode> response = naverWorksUtil.post(accessToken, "/users/me/drive/files", fileMetadata);
        
        System.out.println("파일 전송 body: " + response.getBody());
        System.out.println("파일 전송 Headers: " + response.getHeaders());
        
        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode responseBody = response.getBody();
            String uploadUrl = responseBody.get("uploadUrl").asText();
            
            byte[] fileContent = file.getBytes();
            String filename = file.getOriginalFilename();
            String contentType = file.getContentType();
            ResponseEntity<String> fileresponse = naverWorksUtil.putFile(accessToken,uploadUrl, fileContent,filename,contentType);

            if (fileresponse.getStatusCode().is2xxSuccessful()) {
             
        
            } else {
                throw new RuntimeException("Failed to upload file content: " + fileresponse.getBody());
            }
            
            
        } else {
            throw new RuntimeException("Failed to create file metadata");
        }
		
		
		
	}

}
