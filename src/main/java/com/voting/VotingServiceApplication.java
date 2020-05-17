package com.voting;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableScheduling
@EnableSwagger2
public class VotingServiceApplication {

	@Value("${exchange.voting.result.name}")
	private String topicExchangeName;

	@Value("${queue.voting.result.name}")
	private String queueName;

	public static void main(String[] args) {
		SpringApplication.run(VotingServiceApplication.class, args);
	}

	@Bean
	public TopicExchange exchange() {
		return new TopicExchange(topicExchangeName);
	}

	@Bean
	public Queue queue() {
		return new Queue(queueName, true);
	}

	@Bean
	public Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with("pauta.#");
	}
}
