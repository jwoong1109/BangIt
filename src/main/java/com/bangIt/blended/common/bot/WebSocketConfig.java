package com.bangIt.blended.common.bot;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket 설정 클래스
 * WebSocket 및 STOMP 프로토콜을 설정합니다.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * STOMP 엔드포인트 등록
     * 클라이언트가 연결할 WebSocket 엔드포인트를 설정합니다.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/bangItBot") // WebSocket 엔드포인트 URL
                .withSockJS(); // SockJS를 사용하여 브라우저 호환성 제공
    }

    /**
     * 메시지 브로커 설정
     * 클라이언트와 서버 간의 메시지 라우팅 규칙을 설정합니다.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue"); // SimpleBroker 설정 (브로커가 클라이언트로 메시지를 발송하는 경로)
        config.setApplicationDestinationPrefixes("/app"); // 클라이언트가 서버로 메시지를 보낼 때 사용하는 접두어
        config.setUserDestinationPrefix("/user"); // 사용자별 메시지 목적지 접두어
    }
}
