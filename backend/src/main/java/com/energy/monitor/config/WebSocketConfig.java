package com.energy.monitor.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * This is the "new" piece compared to your booking app: instead of the
 * frontend repeatedly asking "any updates?" (polling), the server pushes
 * new readings out the moment they arrive.
 *
 * Think of it like a radio station: the backend "broadcasts" on a topic
 * (/topic/readings), and any browser tab that has "tuned in" to that
 * topic receives the message instantly, with no extra request needed.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Messages sent to destinations starting with /topic get broadcast
        // to every subscribed client.
        config.enableSimpleBroker("/topic");
        // Messages the client sends to the server go to /app/...
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // The URL the React app connects to. withSockJS() adds a fallback
        // for browsers/networks that block raw WebSocket connections.
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }
}
