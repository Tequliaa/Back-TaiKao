package TaiExam.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocket处理器
 */
@Component
public class ExamWebSocketHandler implements WebSocketHandler {
    private static final Logger log = LoggerFactory.getLogger(ExamWebSocketHandler.class);

    // 存储试卷ID与对应连接的映射（线程安全）
    private static final Map<Integer, CopyOnWriteArraySet<WebSocketSession>> EXAM_CONNECTIONS = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 从URI中提取examId
        String path = session.getUri().getPath();
        String[] pathSegments = path.split("/");
        if (pathSegments.length >= 4) {
            try {
                Integer examId = Integer.parseInt(pathSegments[3]);
                
                // 将连接加入对应试卷的连接集合
                EXAM_CONNECTIONS.computeIfAbsent(examId, k -> new CopyOnWriteArraySet<>()).add(session);
                log.info("WebSocket连接建立：试卷ID={}，当前在线数={}", examId, getOnlineCount(examId));
                
                // 发送连接成功消息
                sendMessageToSession(session, JSON.toJSONString(Map.of(
                    "type", "connect",
                    "payload", Map.of("message", "连接成功", "examId", examId)
                )));
                
            } catch (NumberFormatException e) {
                log.error("无法解析examId: {}", pathSegments[3]);
                session.close();
            }
        } else {
            log.error("WebSocket连接路径格式错误: {}", path);
            session.close();
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (message instanceof TextMessage) {
            String payload = ((TextMessage) message).getPayload();
            log.info("收到WebSocket消息：{}", payload);
            
            try {
                JSONObject msgObj = JSON.parseObject(payload);
                String type = msgObj.getString("type");
                JSONObject data = msgObj.getJSONObject("payload");

                switch (type) {
                    case "subscribe":
                        log.info("用户订阅试卷更新：试卷ID={}, 用户ID={}",
                                data.getInteger("examId"),
                                data.getString("userId"));
                        // 发送订阅确认消息
                        sendMessageToSession(session, JSON.toJSONString(Map.of(
                            "type", "subscribe_success",
                            "payload", Map.of("message", "订阅成功")
                        )));
                        break;
                    case "ping":
                        // 响应心跳
                        sendMessageToSession(session, JSON.toJSONString(Map.of("type", "pong")));
                        break;
                    default:
                        log.warn("未知消息类型：{}", type);
                }
            } catch (Exception e) {
                log.error("处理WebSocket消息失败", e);
            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("WebSocket传输错误", exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.info("WebSocket连接关闭：状态={}", closeStatus);
        
        // 从所有试卷连接中移除该会话
        for (Map.Entry<Integer, CopyOnWriteArraySet<WebSocketSession>> entry : EXAM_CONNECTIONS.entrySet()) {
            if (entry.getValue().remove(session)) {
                log.info("WebSocket连接关闭：试卷ID={}，当前在线数={}", entry.getKey(), getOnlineCount(entry.getKey()));
                
                // 清理空集合
                if (entry.getValue().isEmpty()) {
                    EXAM_CONNECTIONS.remove(entry.getKey());
                }
                break;
            }
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 向指定会话发送消息
     */
    private void sendMessageToSession(WebSocketSession session, String message) {
        try {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(message));
            }
        } catch (IOException e) {
            log.error("发送消息到会话失败", e);
        }
    }

    /**
     * 向指定试卷的所有在线客户端广播消息
     */
    public static void broadcastToExam(Integer examId, Map<String, Object> data) {
        if (!EXAM_CONNECTIONS.containsKey(examId)) {
            log.debug("试卷ID={}无在线连接，无需广播", examId);
            return;
        }

        String message = JSON.toJSONString(Map.of(
                "type", "response_update",
                "payload", data
        ));

        // 遍历所有连接发送消息
        for (WebSocketSession session : EXAM_CONNECTIONS.get(examId)) {
            try {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(message));
                }
            } catch (IOException e) {
                log.error("广播消息失败", e);
            }
        }
    }

    /**
     * 获取指定试卷的在线连接数
     */
    private int getOnlineCount(Integer examId) {
        return EXAM_CONNECTIONS.getOrDefault(examId, new CopyOnWriteArraySet<>()).size();
    }
}
