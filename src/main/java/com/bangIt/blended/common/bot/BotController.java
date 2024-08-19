package com.bangIt.blended.common.bot;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class BotController {

    // 봇 서비스 주입
    private final BotService botService;
    
    // WebSocket 메시지 전송을 위한 템플릿
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/query")
    public void handleQuery(Question dto) {
        System.out.println(">>> 사용자 질문: " + dto);
        botService.handleUserQuery(dto);
    }
    
    /*
    // 최초인사말
    @MessageMapping("/hello")
    public void hello(Question dto) {
        System.out.println(">>> 초기 인사: " + dto);
        String key = dto.getKey();
        String pattern = "{0}님 안녕하세요! 숙소 검색이 필요하시면 '[지역] [상세주소] [숙소 유형] 알려줘'라고 말씀해 주세요.";
        // 초기 인사 메시지 전송
        messagingTemplate.convertAndSend("/topic/bot/" + key,
                MessageFormat.format(pattern, dto.getName()));
    }*/
}