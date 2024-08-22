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
@Controller // 이 클래스가 Spring MVC 컨트롤러임을 나타냅니다.
@RequiredArgsConstructor // Lombok 어노테이션으로, final 필드에 대한 생성자를 자동으로 생성합니다.
public class BotController {

	// 봇 서비스 주입
	// 이 서비스는 실제 챗봇 로직을 처리합니다. (예: 사용자 입력 분석, 응답 생성)
	private final BotService botService;

	// WebSocket 메시지 전송을 위한 템플릿
	// 이 템플릿을 통해 서버에서 클라이언트로 메시지를 전송할 수 있습니다.
	private final SimpMessagingTemplate messagingTemplate;

	/**
	 * WebSocket을 통해 들어오는 사용자 쿼리를 처리합니다. '/query' 주소로 들어오는 WebSocket 메시지를 처리합니다.
	 * 
	 * @param dto 사용자의 질문 정보를 담고 있는 객체
	 */
	@MessageMapping("/query")

	public void handleQuery(Question dto) {
	    System.out.println(">>> 사용자 질문: " + dto);
	    String response = botService.processInput(dto.getContent());

	    // JSON 형식으로 메시지 전송
	    String jsonResponse = String.format("{\"content\": \"%s\"}", response);
	    messagingTemplate.convertAndSend("/topic/responses", jsonResponse);
	}


	/**
	 * HTTP POST 요청을 통해 들어오는 챗봇 대화를 처리합니다. 이 메소드는 인증된 사용자만 접근할 수 있습니다.
	 * 
	 * @param input 사용자 입력 (채팅 메시지)
	 * @return 챗봇의 응답
	 */
	@PostMapping("/chat") // '/chat' URL로 들어오는 POST 요청을 처리합니다.
	@PreAuthorize("isAuthenticated()") // Spring Security를 사용하여 인증된 사용자만 이 메소드에 접근할 수 있도록 합니다.
	public String chat(@RequestBody String input) {
		// botService를 사용하여 사용자 입력을 처리하고 응답을 생성합니다.
		return botService.processInput(input);
	}
}