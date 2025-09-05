package org.example.webrtcdemo;

import lombok.RequiredArgsConstructor;
import org.example.webrtcdemo.kurento.KurentoHandler;
import org.example.webrtcdemo.webrtc.WebRtcHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@RequiredArgsConstructor
@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {
    private final WebRtcHandler webRtcHandler;
    private final KurentoHandler kurentoHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webRtcHandler, "/webrtc").setAllowedOriginPatterns("*");
        registry.addHandler(kurentoHandler, "/kurento").setAllowedOriginPatterns("*");
    }

    @Bean
    public ServletServerContainerFactoryBean createServletServerContainerFactoryBean() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(32768);
        return container;
    }
}
