// src/test/java/id/ac/ui/cs/advprog/perbaikiinaja/Config/WebSocketConfigTest.java
package id.ac.ui.cs.advprog.perbaikiinaja.Config;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.SockJsServiceRegistration;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.StompWebSocketEndpointRegistration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WebSocketConfigTest {

    @Test
    void configureMessageBroker_shouldEnableBrokerAndSetPrefixes() {
        MessageBrokerRegistry registry = mock(MessageBrokerRegistry.class);
        WebSocketConfig config = new WebSocketConfig();

        config.configureMessageBroker(registry);

        verify(registry).enableSimpleBroker("/topic");
        verify(registry).setApplicationDestinationPrefixes("/app");
    }

    @Test
    void registerStompEndpoints_shouldRegisterWsEndpointWithSockJs() {
        // Arrange
        StompEndpointRegistry registry = mock(StompEndpointRegistry.class);
        StompWebSocketEndpointRegistration stompRegistration = mock(StompWebSocketEndpointRegistration.class);
        SockJsServiceRegistration sockJsRegistration = mock(SockJsServiceRegistration.class);

        when(registry.addEndpoint("/ws")).thenReturn(stompRegistration);
        when(stompRegistration.setAllowedOriginPatterns("*")).thenReturn(stompRegistration);
        when(stompRegistration.withSockJS()).thenReturn(sockJsRegistration);

        WebSocketConfig config = new WebSocketConfig();

        // Act
        config.registerStompEndpoints(registry);

        // Assert
        verify(registry).addEndpoint("/ws");
        verify(stompRegistration).setAllowedOriginPatterns("*");
        verify(stompRegistration).withSockJS();
    }
}