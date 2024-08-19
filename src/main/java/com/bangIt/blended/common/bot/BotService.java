package com.bangIt.blended.common.bot;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BotService {
    private final SimpMessagingTemplate messagingTemplate;
    private final KomoranService komoranService;

    /**
     * 사용자 질문을 처리하는 메서드
     * @param dto 사용자 질문 데이터 전송 객체
     */
    public void handleUserQuery(Question dto) {
        if (dto.getContent().contains("예약한 숙소 정보")) {
            String response = getReservationInfoLink(dto.getUserId());
            messagingTemplate.convertAndSend("/topic/bot/" + dto.getKey(), 
                "{\"content\":\"" + response.replace("\"", "\\\"") + "\"}");
        } else {
            // 다른 응답 처리
            String response = "죄송합니다. 제가 답변할 수 없는 질문이네요!";
            messagingTemplate.convertAndSend("/topic/bot/" + dto.getKey(), 
                "{\"content\":\"" + response + "\"}");
        }
    }

    /**
     * 예약 정보 링크를 생성하는 메서드
     * @param userId 사용자 ID
     * @return 예약 정보 페이지 링크
     */
    private String getReservationInfoLink(String userId) {
        return "예약하신 숙소 정보는 다음 링크에서 확인하실 수 있습니다: /reservations/" + userId;
    }

    /**
     * 일반 질문을 처리하는 메서드
     * @param dto 사용자 질문 데이터 전송 객체
     */
    private void handleGeneralQuestion(Question dto) {
        String answer = "죄송합니다. 제가 답변할 수 없는 질문이네요!";
        messagingTemplate.convertAndSend("/topic/bot/" + dto.getKey(), answer);
    }
}