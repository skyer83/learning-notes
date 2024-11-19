package com.tanji168.websocket.java;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 监听 websocket 地址：/javaWs
 */
@ServerEndpoint(value = "/javaWs")
@Component
@Slf4j
public class WsServerEndpoint {

    /** ConcurrentHashMap 线程安全 */
    private static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    /**
     * 建立链接
     * @author shenjh
     * @since 2024/11/18 16:17
     */
    @OnOpen
    public void onOpen(Session session) {
        // 建立链接：0
        log.info("建立链接：" + session.getId());
        sessionMap.put(session.getId(), session);

        sendMessageSingle(session, buildOpenMessage(session.getId()));
    }

    /**
     * 接收消息
     * @param message
     * @author shenjh
     * @since 2024/11/18 16:18
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("接收消息: {}", message);
        sendMessageMass(message);
    }

    /**
     * 关闭链接
     * @author shenjh
     * @since 2024/11/18 16:19
     */
    @OnClose
    public void onClose(Session session) {
        log.info("关闭链接：" + session.getId());
        sessionMap.remove(session.getId());
    }

    /**
     * 执行异常时的托底方法
     * @param throwable
     * @author shenjh
     * @since 2024/11/18 16:19
     */
    @OnError
    public void onError(Throwable throwable, Session session) {
        log.error("执行异常！", throwable);

        if (session.isOpen()) {
            try {
                session.close();
            } catch (IOException e) {
                log.error("", e);
            }
        }
    }

    /**
     * 群发，每隔 300 秒往客户端发送消息
     * @author shenjh
     * @since 2024/11/19 11:15
     */
    @Scheduled(fixedRate = 300000)
    public void sendMessage() {
        sessionMap.forEach((k, v) -> {
            v.getAsyncRemote().sendText("心跳");
        });
    }

    /**
     * 群发消息
     * @param message
     * @author shenjh
     * @since 2024/11/19 15:01
     */
    public static void sendMessageMass(String message) {
        sessionMap.forEach((k, v) -> {
            v.getAsyncRemote().sendText(message);
        });
    }

    /**
     * 单发送消息给客户端
     * @param message
     */
    public static void sendMessageSingle(Session session, JSONObject message) {
        session.getAsyncRemote().sendText(message.toString());
    }

    /**
     * 构建告知客户端建立的 websocket 连接的 sessionId
     * @param sessionId
     * @return cn.hutool.json.JSONObject
     * @author shenjh
     * @since 2024/11/19 12:36
     */
    public static JSONObject buildOpenMessage(String sessionId) {
        return new JSONObject().set("type", "open").set("sessionId", sessionId);
    }
}
