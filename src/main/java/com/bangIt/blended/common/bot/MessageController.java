package com.bangIt.blended.common.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate; // RabbitTemplate 추가

@Controller
@RequiredArgsConstructor
public class MessageController {

	/*
	 * private final SimpMessagingTemplate messagingTemplate; // WebSocket 메시지 전송용
	 * private final RabbitTemplate rabbitTemplate; // RabbitMQ 메시지 전송용
	 * 
	 * @Value("${spring.rabbitmq.template.exchange}") private String exchange; //
	 * RabbitMQ 교환기 (Exchange) 이름
	 * 
	 * @Value("${spring.rabbitmq.template.routing-key}") private String routingKey;
	 * // RabbitMQ 라우팅 키 (Routing Key)
	 * 
	 * // 클라이언트가 '/app/bot'으로 전송한 메시지를 처리하는 메서드
	 * 
	 * @MessageMapping("/bot") public void handleMessage(Question question) { //
	 * 클라이언트에서 받은 메시지를 로그에 출력 (디버깅용) System.out.println("Received message: " +
	 * question);
	 * 
	 * // 메시지를 RabbitMQ로 전송 // RabbitTemplate을 사용하여 RabbitMQ로 메시지를 전송합니다. //
	 * 'exchange'는 RabbitMQ에서 메시지를 라우팅할 교환기 이름이며, 'routingKey'는 라우팅 키입니다.
	 * rabbitTemplate.convertAndSend(exchange, routingKey, question);
	 * 
	 * // 클라이언트에게 응답 메시지를 전송 // WebSocket을 통해 클라이언트에게 응답 메시지를 전송합니다. //
	 * '/topic/bot'은 클라이언트가 구독하는 경로입니다. String responseMessage = "안녕하세요, " +
	 * question.getName() + "님!"; messagingTemplate.convertAndSend("/topic/bot",
	 * responseMessage); }
	 */
}
