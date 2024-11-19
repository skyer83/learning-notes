# 介绍

视频教程地址：[WebSocket入门与案例实战](https://www.bilibili.com/video/BV1hF411k7bj?spm_id_from=333.788.videopod.episodes&vd_source=f050b4d563f8e729f80ae8b3803dfe24)

视频源码地址：https://gitee.com/duoli-java/websocket-demo

# websocket-demo

## 介绍

WebSocket 基于 java 和 springboot 的两种方式实现服务端；使用 `html + js` 实现客户端

## 软件架构

springboot项目

## 使用说明

### java 目录

java 目录是基于java注解的实现

### spring 目录
spring 目录是基于spring封装的实现
> HttpSessionHandshakelnterceptor(抽象类)：握手拦截器，在握手前后添加操作
> 
> AbstractWebSocketHandler(抽象类)：WebSocket处理程序，监听连接前，连接中，连接后
> 
> WebSocketConfigurer(接口)：配置程序，比如配置监听哪个端口，上面的握手拦截器，处理程序的使用

## 验证说明
请求地址：http://localhost:8080 ，默认访问的页面是 resources 下的 static/index.html

java 验证地址：http://localhost:8080/java-ws-client.html

spring 验证地址：http://localhost:8080/spring-ws-client.html

# 参考文章

## [SpringBoot+WebSocket 消息推送 校验 心跳机制 PING-PONG 用户分组等](https://blog.csdn.net/TCLms/article/details/131321389)

```java
/**
 * webSocket服务
 */
@Slf4j
@Component
@ServerEndpoint("/wsserver/{groupId}/{userId}")
public class WebSocketServer {
 
    private static WebSocketGroupManager groupManager;
    private ScheduledExecutorService executor= Executors.newSingleThreadScheduledExecutor();
    @Autowired
    public void setWebSocketGroupManager(WebSocketGroupManager manager) {
        groupManager = manager;
    }
 
    @OnOpen
    public void onOpen(Session session, @PathParam("groupId") String groupId, @PathParam("userId") String userId) {
        try {
            //包含token校验
            String queryString = session.getQueryString();
            Map<String, List<String>> queryParams = decodeQueryString(queryString);
            // 获取token参数
            List<String> tokenValues = queryParams.get("token");
            if (tokenValues != null && !tokenValues.isEmpty()) {
                //根据自己的param顺序，实现不同的业务逻辑
                String token = tokenValues.get(0);
                // 进行校验操作
                if (isValidToken(token)) {
                    WebSocketGroup group = groupManager.getOrCreateGroup(groupId);
                    WebSocketUser user = new WebSocketUser(session, userId,token);
                    group.addUser(user);
                    // 更新在线用户计数器
                    int count = groupManager.increaseOnlineCount();
 
                    session.getAsyncRemote().sendPing(ByteBuffer.wrap(new byte[0]));
 
                    log.info("校验通过!用户：{} 上线，当前在线人数：{}，分组：{}，分组在线人数：{}", userId, count, groupId,group != null ? group.getUserCount() : 0);
                    // 校验通过，进行其他操作
                } else {
                    // 校验失败，关闭连接
                    closeSession(session,groupId,userId);
                    log.warn("校验失败!用户：{} token 错误!",userId);
                }
            } else {
                // 没有提供token参数，直接过滤，关闭连接
                closeSession(session,groupId,userId);
            }
        } catch (Exception e) {
            log.error("用户：{} ，连接时发送异常!异常信息：{}", userId, e.getMessage());
            closeSession(session, groupId, userId);
        }
    }
 
    // 接收普通消息
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("groupId") String groupId, @PathParam("userId") String userId) {
        WebSocketGroup group = groupManager.getGroup(groupId);
        if (group != null) {
            group.sendMessageToAllUsers(message);
        }
    }
 
    // 接收心跳消息
    @OnMessage
    public void onPong(PongMessage pong, Session session, @PathParam("groupId") String groupId, @PathParam("userId") String userId) {
        executor.schedule(() -> {
            try {
                // 发送空的Ping消息
                session.getAsyncRemote().sendPing(ByteBuffer.wrap(new byte[0]));
            } catch (IOException e) {
                // 处理发送失败的情况
                log.error("Ping 用户：{} 心跳异常，关闭会话，错误原因：{}", userId, e.getMessage());
                closeSession(session, groupId, userId);
            }
        }, 30, TimeUnit.SECONDS);
    }
 
    @OnClose
    public void onClose(@PathParam("groupId") String groupId, @PathParam("userId") String userId, Session session) {
        try {
            WebSocketGroup group = groupManager.getGroup(groupId);
            if (group != null) {
                group.removeUser(userId);
                // 检查分组的用户数量，如果为0，则从分组管理器中删除分组对象
                if (group.getUserCount() == 0) {
                    groupManager.removeGroup(groupId);
                }
            }
            // 更新在线用户计数器
            int count = groupManager.decreaseOnlineCount();
 
            log.info("用户：{} 退出，当前在线人数：{}，分组：{}，分组在线人数：{}", userId, count, groupId, group != null ? group.getUserCount() : 0);
        } catch (Exception e) {
            log.error("连接关闭时异常！用户：{}，分组：{}，错误原因：{}", userId, groupId, e.getMessage());
            closeSession(session, groupId, userId);
        }
    }
 
    @OnError
    public void onError(Throwable throwable, @PathParam("groupId") String groupId, @PathParam("userId") String userId, Session session) {
        // 向客户端发送错误消息
        session.getAsyncRemote().sendText("发生了错误，请稍后再试。");
        log.error("连接异常！用户：{}，分组：{}，错误原因：{}", userId, groupId, throwable.getMessage());
        closeSession(session, groupId, userId);
    }
 
    /**
     * 关闭Session
     *
     * @param session
     */
    private void closeSession(Session session, String groupId, String userId) {
        // 关闭连接
        if (session != null && session.isOpen()) {
            //关闭后删除掉对应用户信息
            WebSocketGroup group = groupManager.getGroup(groupId);
            if (group != null) {
                group.removeUser(userId);
                // 检查分组的用户数量，如果为0，则从分组管理器中删除分组对象
                if (group.getUserCount() == 0) {
                    groupManager.removeGroup(groupId);
                }
            }
            // 更新在线用户计数器
            int count = groupManager.decreaseOnlineCount();
            try {
                session.close();
            } catch (IOException e) {
                log.error("关闭session会话时异常：{}", e.getMessage());
            }
        }
    }
 
    /**
     * 获取全部在线用户数量统计
     *
     * @return
     */
    public static int getOnlineCount() {
        return groupManager.getOnlineCount();
    }
 
    /**
     * 向所有分组的子目录下发命令。
     *
     * @param message
     * @warn 由服务器统一下发，若使用多线程，存在线程安全问题。
     */
    public static void sendMessageToAllGroups(String message) {
        groupManager.sendMessageToAllGroups(message);
    }
 
    // 校验token的方法
    private boolean isValidToken(String token) {
        // 根据自己的需求，进行校验逻辑，返回校验结果
        return true;
    }
 
    // 解码查询参数
    private Map<String, List<String>> decodeQueryString(String queryString) {
        // 根据自己的需求实现解码逻辑
        //这里做简单的解析参数。
        Map<String, List<String>> queryParams = new HashMap<>();
        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            String[] parts = pair.split("=");
            String name = parts[0];
            String value = "";
            if (parts.length > 1) {
                value = parts[1];
            }
            queryParams.computeIfAbsent(name, k -> new ArrayList<>()).add(value);
        }
        return queryParams;
    }
}
```
