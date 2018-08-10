package com.example.websocket;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@SpringBootApplication
public class SpringWebsocketApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringWebsocketApplication.class, args);
	}
}

@Configuration
@EnableScheduling
class MessageSender {

	@Autowired
    private SimpMessageSendingOperations messagingTemplate;

	@Scheduled(fixedRate = 1000)
    public void sendMessage() {
		messagingTemplate.convertAndSend("/topic/messages", "Message at " + new Date());
    }
}

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig implements WebSocketMessageBrokerConfigurer  {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
        	.addEndpoint("/msg-service")
        	.withSockJS()
        	.setClientLibraryUrl("https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js");
    }
}
