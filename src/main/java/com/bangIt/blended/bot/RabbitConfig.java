package com.bangIt.blended.bot;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Spring Boot의 자동 구성을 사용하여 RabbitMQ를 설정하는 Configuration 클래스입니다.
// @Slf4j: Lombok을 사용하여 로그를 출력할 수 있는 기능을 추가합니다.
// @RequiredArgsConstructor: Lombok을 사용하여 final 필드를 가진 생성자를 자동으로 생성합니다.
@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableRabbit // Spring RabbitMQ의 자동 설정을 활성화합니다.
public class RabbitConfig {

	// TopicExchange는 메시지를 발행할 교환기(Exchange)입니다.
	// TopicExchange는 라우팅 키를 기반으로 메시지를 큐에 전달합니다.
	@Bean
	public TopicExchange topicExchange() {
		// 'my.exchange'라는 이름의 TopicExchange를 생성합니다.
		return new TopicExchange("my.exchange");
	}

	// 큐(Queue)는 메시지를 저장하는 장소입니다.
	// 큐는 메시지를 저장하고 이를 소비자(Consumer)가 가져갈 수 있도록 합니다.
	@Bean
	public Queue queue() {
		// 'my.queue'라는 이름의 큐를 생성합니다. 큐는 내구성(true)으로 설정되어 있어 서버 재시작 시에도 유지됩니다.
		return new Queue("my.queue", true);
	}

	// Binding은 Exchange와 Queue를 연결하는 역할을 합니다.
	// 메시지가 교환기를 통해 큐로 전달되도록 라우팅 규칙을 설정합니다.
	@Bean
	public Binding binding(Queue queue, TopicExchange exchange) {
		// 'my.queue' 큐와 'my.exchange' TopicExchange를 연결합니다.
		// 'my.routing.key' 라우팅 키를 사용하여 메시지를 큐로 라우팅합니다.
		return BindingBuilder.bind(queue).to(exchange).with("my.routing.key");
	}

	// RabbitTemplate은 RabbitMQ에 메시지를 전송하고 응답을 받기 위한 템플릿입니다.
	// ConnectionFactory를 사용하여 RabbitMQ 서버와의 연결을 관리합니다.
	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		// ConnectionFactory를 사용하여 RabbitTemplate을 생성합니다.
		// RabbitTemplate은 메시지를 RabbitMQ로 보내고 받을 때 사용됩니다.
		return new RabbitTemplate(connectionFactory);
	}

	// RabbitAdmin은 RabbitMQ에서 관리 작업(예: 큐, 교환기, 바인딩 생성)을 수행하는 데 사용됩니다.
	// RabbitAdmin을 사용하여 애플리케이션이 시작될 때 큐, 교환기 및 바인딩을 자동으로 생성할 수 있습니다.
	@Bean
	public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
		// ConnectionFactory를 사용하여 RabbitAdmin을 생성합니다.
		return new RabbitAdmin(connectionFactory);
	}
}
