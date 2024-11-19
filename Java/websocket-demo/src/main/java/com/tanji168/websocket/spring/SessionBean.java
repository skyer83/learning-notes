package com.tanji168.websocket.spring;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author shenjh
 * @version 1.0
 * @since 2024/11/19 15:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionBean {

    private WebSocketSession webSocketSession;

    private Integer clientId;

}
