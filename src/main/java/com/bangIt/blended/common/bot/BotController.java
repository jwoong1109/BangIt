package com.bangIt.blended.common.bot;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class BotController {

    // 봇 서비스 주입
    private final BotService botService;
    
    // WebSocket 메시지 전송을 위한 템플릿
    private final SimpMessagingTemplate messagingTemplate;
    
    /**
    @MessageMapping("/query")
    public void handleQuery(Question dto) {
        System.out.println(">>> 사용자 질문: " + dto);
        botService.handleUserQuery(dto);
    }*/
    
    /**
     * 챗봇과의 대화를 처리하는 엔드포인트
     * 인증된 사용자만 접근 가능합니다.
     * @param input 사용자 입력
     * @return 챗봇 응답
     */
    @PostMapping("/chat")
    @PreAuthorize("isAuthenticated()")
    public String chat(@RequestBody String input) {
        return botService.processInput(input);
    }
    
}