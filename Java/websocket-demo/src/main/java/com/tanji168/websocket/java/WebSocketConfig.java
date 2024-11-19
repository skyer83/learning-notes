package com.tanji168.websocket.java;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 *
 * @author shenjh
 * @version 1.0
 * @since 2024/11/19 11:47
 */
@Configuration
public class WebSocketConfig {

    /**
     * 注入 ServerEndpointExporter，这个 bean 会自动注册使用了 @ServerEndpoint 注解声明的对象，否则就会报错。<br/>
     * 注解如：@ServerEndpoint(value = "/javaWs")，配置好之后，就可以通过 ws://127.0.0.1:8080/javaWs 来连接这个 websocket 了。
     * @return org.springframework.web.socket.server.standard.ServerEndpointExporter
     * @author shenjh
     * @since 2024/11/19 11:52
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
