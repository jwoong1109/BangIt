package com.bangIt.blended.controller.chatbot;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.bangIt.blended.common.bot.Question;

import lombok.RequiredArgsConstructor;

// Lombok의 @RequiredArgsConstructor 어노테이션을 사용하여 final 필드를 초기화하는 생성자를 자동으로 생성합니다.
@RequiredArgsConstructor
@Controller
public class BotController {

	/*
	 * // RabbitTemplate은 RabbitMQ와 상호작용하기 위한 클래스입니다. // 의존성 주입을 통해 자동으로 주입됩니다.
	 * private final RabbitTemplate template;
	 * 
	 * // application.properties 또는 application.yml에서 설정된 RabbitMQ의 교환기 이름을 주입합니다.
	 * 
	 * @Value("${spring.rabbitmq.template.exchange}") private String exchange;
	 * 
	 * // application.properties 또는 application.yml에서 설정된 RabbitMQ의 라우팅 키를 주입합니다.
	 * 
	 * @Value("${spring.rabbitmq.template.routing-key}") private String routingKey;
	 * 
	 * // ***WebSocket 메시지를 처리하기 위한 메서드***
	 * 
	 * @MessageMapping("/bot") public void bot(Question dto) { // RabbitMQ의
	 * 교환기(exchange)와 라우팅 키(routingKey)를 사용하여 메시지를 전송합니다. // convertAndSend() 메서드는
	 * RabbitMQ에 메시지를 전송하는 기능을 제공합니다. template.convertAndSend(exchange, routingKey,
	 * dto); }
	 */

}
