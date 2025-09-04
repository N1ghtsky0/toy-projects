package org.example.webrtcdemo.webrtc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WebRtcHandler extends TextWebSocketHandler {
    private static final Gson gson = new GsonBuilder().create();

    private final Map<String, WebSocketSession> participants = new ConcurrentHashMap<>();
    private static boolean isBroadcasting = false;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String id = session.getId();
        if (!participants.containsKey(id)) {
            participants.put(id, session);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", "join");
            jsonObject.addProperty("key", id);
            sendMessage(jsonObject.toString(), session);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        participants.remove(session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JsonObject jsonObject = gson.fromJson(message.getPayload(), JsonObject.class);
        log.info("[RECEIVED] {}", jsonObject.toString());

        JsonObject response = new JsonObject();
        final String id = jsonObject.get("id").getAsString();
        switch (id) {
            case "startBroadcast":
                if (!isBroadcasting) {
                    response.addProperty("id", "viewerList");
                    response.add("result", participants.keySet().stream().map(String::new).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
                    sendMessage(response.toString(), session);
                } else {
                    response.addProperty("id", id + "Reject");
                    response.addProperty("result", "already broadcasting");
                    sendMessage(response.toString(), session);
                }
                break;
            case "stopBroadcast":
                isBroadcasting = false;
                participants.forEach((participantId, participantSession) -> {
                    response.addProperty("id", "broadcastStop");
                    sendMessage(response.toString(), participantSession);
                });
                break;
            case "icecandidate":
            case "offer":
            case "answer":
                response.addProperty("id", id);
                response.add("result", jsonObject.get("result").getAsJsonObject());
                response.addProperty("key", session.getId());
                sendMessage(response.toString(), participants.get(jsonObject.get("to").getAsString()));
                break;
            default:
                log.warn("[UNKNOWN MESSAGE] {}", jsonObject);
                break;
        }
    }

    private synchronized void sendMessage(String message, WebSocketSession session) {
        try {
            log.info("[SEND] {}", message);
            session.sendMessage(new TextMessage(message));
        } catch (Exception e) {
            log.error("Failed to send message: {}", message, e);
        }
    }
}
