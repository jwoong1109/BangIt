package com.bangIt.blended.common.bot;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import lombok.RequiredArgsConstructor;

/**
 * 이 클래스는 챗봇 시스템의 컨트롤러 역할을 합니다. WebSocket 통신과 HTTP 요청을 모두 처리하여 사용자와 챗봇 간의 상호작용을
 * 관리합니다.
 */
@Controller // Spring MVC의 컨트롤러 역할을 수행하는 클래스
@RequiredArgsConstructor // Lombok 어노테이션으로 생성자 자동 생성
public class BotController {

	private final BotService botService; // 봇 서비스 주입
	private final SimpMessagingTemplate messagingTemplate; // WebSocket 메시지 전송을 위한 템플릿

	/**
	 * WebSocket을 통해 들어오는 사용자 쿼리를 처리합니다. 
	 * '/query' 경로로 들어오는 WebSocket 메시지를 처리합니다.
	 * @param dto 사용자 질문 정보를 담고 있는 객체
	 */
	@MessageMapping("/query")
	public void handleQuery(Question dto) {
		System.out.println(">>> 사용자 질문: " + dto);
		// 사용자 입력을 처리하여 챗봇 응답 생성
		String response = botService.processInput(dto.getContent());

		// JSON 형식으로 응답 메시지 전송
		String jsonResponse = String.format("{\"content\": \"%s\"}", response);
		System.out.println("Sending response: " + jsonResponse); // 추가된 로그
		messagingTemplate.convertAndSend("/topic/responses", jsonResponse);
	}

	/**
	 * HTTP POST 요청을 통해 들어오는 챗봇 대화를 처리합니다. 
	 * 인증된 사용자만 접근할 수 있습니다.
	 * @param input 사용자 입력 (채팅 메시지)
	 * @return 챗봇의 응답
	 */
	@PostMapping("/chat") // '/chat' URL로 들어오는 POST 요청 처리
	@PreAuthorize("isAuthenticated()") // 인증된 사용자만 접근 가능
	public String chat(@RequestBody String input) {
		// 사용자 입력을 처리하여 챗봇 응답 생성
		return botService.processInput(input);
	}
}
