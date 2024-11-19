package com.tanji168.websocket.spring;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author shenjh
 * @version 1.0
 * @since 2024/11/19 15:54
 */
@Slf4j
@Component
public class WsHandler extends AbstractWebSocketHandler {

    private final static Map<String, SessionBean> sessionMap = new ConcurrentHashMap<>();
    private final static AtomicInteger atomicInteger = new AtomicInteger(0);

    /**
     * 建立链接
     * @param session 会话
     * @author shenjh
     * @since 2024/11/19 16:11
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);

        log.info("建立链接：" + session.getId());
        SessionBean sessionBean = new SessionBean(session, atomicInteger.getAndIncrement());
        sessionMap.put(session.getId(), sessionBean);

        // 告知客户端自己的 clientId
        sendMessageSingle(session, buildOpenMessage(session.getId()));
        // 群通知
        sendMessageMass(new TextMessage("clientId[" + sessionBean.getClientId() + "]进入聊天室"));
    }

    /**
     * 接受消息，先进入本方法，由本方法判断消息类型：TextMessage、BinaryMessage、PongMessage 后再调用对应的方法
     * @param session 会话
     * @param message 消息
     * @author shenjh
     * @since 2024/11/19 16:11
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);
    }

    /**
     * 接收文本消息
     * @param session 会话
     * @param message 消息
     * @author shenjh
     * @since 2024/11/19 16:12
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);

        log.info("接收消息: {}", message);
        // 群发消息
        sendMessageMass(message);
    }

    /**
     * 接收二进制消息
     * @param session 会话
     * @param message 消息
     * @author shenjh
     * @since 2024/11/19 16:12
     */
    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        super.handleBinaryMessage(session, message);
    }

    /**
     * 接收 PONG 消息（PING的回复就是PONG）
     * @param session 会话
     * @param message 消息
     * @throws Exception 异常信息
     */
    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        super.handlePongMessage(session, message);
    }

    /**
     * 关闭链接
     * @param session 会话
     * @param status 关闭状态
     * @author shenjh
     * @since 2024/11/19 16:25
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);

        log.info("关闭链接：" + session.getId());
        SessionBean sessionBean = sessionMap.get(session.getId());
        // 移除会话
        sessionMap.remove(session.getId());
        // 群通知
        sendMessageMass(new TextMessage("clientId[" + sessionBean.getClientId() + "]退出聊天室"));
    }

    /**
     * 执行异常兜底方法
     * @param session 会话
     * @param exception 异常
     * @author shenjh
     * @since 2024/11/19 16:24
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);

        log.error("执行异常！", exception);
        if (session.isOpen()) {
            session.close();
        }
    }

    /**
     * 群发消息
     * @param message 消息
     * @author shenjh
     * @since 2024/11/19 15:01
     */
    public static void sendMessageMass(TextMessage message) throws IOException {
        for (Map.Entry<String, SessionBean> entry : sessionMap.entrySet()) {
            entry.getValue().getWebSocketSession().sendMessage(message);
        }
    }

    /**
     * 单发送消息给客户端
     * @param message 消息
     */
    public static void sendMessageSingle(WebSocketSession webSocketSession, JSONObject message) throws IOException {
        webSocketSession.sendMessage(new TextMessage(message.toString()));
    }

    /**
     * 构建告知客户端建立的 websocket 连接的 sessionId
     * @param sessionId 会话ID
     * @return cn.hutool.json.JSONObject
     * @author shenjh
     * @since 2024/11/19 12:36
     */
    public static JSONObject buildOpenMessage(String sessionId) {
        return new JSONObject().set("type", "open").set("sessionId", sessionId);
    }
}
