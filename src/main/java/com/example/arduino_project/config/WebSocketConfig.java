package com.example.arduino_project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 클라이언트에서 받을 메시지 경로를 "/topic"으로 설정
        config.enableSimpleBroker("/topic");
        // 클라이언트가 메시지를 보낼 경로를 "/app"으로 설정
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // WebSocket 연결 경로 설정
        registry.addEndpoint("/ws").withSockJS(); // SockJS를 지원하도록 설정
        // CORS 설정을 추가하여 모든 도메인에서 WebSocket 연결 허용
        registry.addEndpoint("/ws").setAllowedOrigins("*");
    }
}
