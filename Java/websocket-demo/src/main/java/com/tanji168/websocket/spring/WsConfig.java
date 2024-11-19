package com.tanji168.websocket.spring;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author shenjh
 * @version 1.0
 * @since 2024/11/19 16:57
 */
@Slf4j
@Configuration
@EnableWebSocket
@AllArgsConstructor
public class WsConfig implements WebSocketConfigurer {

    private WsHandshakeInterceptor wsHandshakeInterceptor;
    private WsHandler wsHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 运行跨域请求
        registry.addHandler(wsHandler, "/springWs").addInterceptors(wsHandshakeInterceptor).setAllowedOrigins("*");
    }
}
