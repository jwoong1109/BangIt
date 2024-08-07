package com.bangIt.blended.bot;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class SocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// STOMP 프로토콜을 사용할 WebSocket 엔드포인트를 등록
        // /ws 엔드포인트를 설정하고 SockJS를 사용하여 브라우저의 WebSocket 지원이 없을 경우 대체 전송 방식 사용
		// .setAllowedOrigins("*") 모든 도메인에서 이 웹소켓 엔드포인트에 접근 허용
        registry.addEndpoint("/bangItBot").setAllowedOrigins("*").withSockJS();
	}
	
	@Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 클라이언트가 서버로 메시지를 보낼 때 사용할 접두사를 설정
        registry.setApplicationDestinationPrefixes("/bot"); // 예: 클라이언트가 /bot/hello로 메시지를 전송하면 해당 메시지는 컨트롤러로 라우팅

        // 서버가 클라이언트에게 메시지를 보낼 때 사용할 주제를 설정
        registry.enableSimpleBroker("/topic"); // 예: 클라이언트는 /topic/greetings 주제를 구독할 수 있음
    }
}
