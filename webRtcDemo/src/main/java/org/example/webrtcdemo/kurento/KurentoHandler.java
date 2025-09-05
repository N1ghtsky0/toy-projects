package org.example.webrtcdemo.kurento;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kurento.client.IceCandidate;
import org.kurento.client.KurentoClient;
import org.kurento.client.MediaPipeline;
import org.kurento.client.WebRtcEndpoint;
import org.kurento.jsonrpc.JsonUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Component
public class KurentoHandler extends TextWebSocketHandler {
    private final static Gson gson = new GsonBuilder().create();
    private final KurentoClient kurentoClient;

    private final Map<String, KurentoUserSession> participants = new ConcurrentHashMap<>();
    private String presenterId = "";
    private MediaPipeline pipeline;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if (!participants.containsKey(session.getId())) {
            participants.put(session.getId(), new KurentoUserSession(session));
            if (!presenterId.isEmpty()) {
                requestViewerOffer(participants.get(session.getId()).getSession());
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        participants.remove(session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JsonObject jsonMessage = gson.fromJson(message.getPayload(), JsonObject.class);
        log.info("[RECEIVED] {}", jsonMessage.toString());

        final String id = jsonMessage.get("id").getAsString();
        switch (id) {
            case "presenter":
                handlePresenter(session, jsonMessage);
                if (!presenterId.isEmpty()) { requestViewerOffer(); }
                break;
            case "viewer":
                handleViewer(session, jsonMessage);
                break;
            case "icecandidate":
                JsonObject candidate = jsonMessage.get("result").getAsJsonObject();
                IceCandidate iceCandidate = gson.fromJson(candidate, IceCandidate.class);

                KurentoUserSession userSession = participants.get(session.getId());
                userSession.addCandidate(iceCandidate);
                break;
            default:
                log.warn("[UNKNOWN MESSAGE] {}", jsonMessage);
                break;
        }
    }

    private synchronized void requestViewerOffer() {
        participants.forEach((viewerId, viewerSession) -> {
            if (!viewerId.equals(presenterId)) {
                requestViewerOffer(viewerSession.getSession());
            }
        });
    }

    private synchronized void requestViewerOffer(WebSocketSession session) {
        JsonObject response = new JsonObject();
        response.addProperty("id", "viewerOffer");
        sendMessage(response.toString(), session);
    }

    private synchronized void handlePresenter(WebSocketSession session, JsonObject message) {
        JsonObject response = new JsonObject();
        response.addProperty("id", "presenterAnswer");
        if (presenterId.isEmpty()) {
            KurentoUserSession presenter = participants.get(session.getId());

            presenterId = session.getId();
            pipeline = kurentoClient.createMediaPipeline();
            presenter.setWebRtcEndpoint(new WebRtcEndpoint.Builder(pipeline).build());

            WebRtcEndpoint presenterWebRtcEndpoint = presenter.getWebRtcEndpoint();

            presenterWebRtcEndpoint.addIceCandidateFoundListener(event -> {
                JsonObject result = new JsonObject();
                result.addProperty("id", "icecandidate");
                result.add("result", JsonUtils.toJsonObject(event.getCandidate()));
                sendMessage(result.toString(), presenter.getSession());
            });

            String presenterOffer = message.get("sdpOffer").getAsJsonObject().get("sdp").getAsString();
            String answerFromKMS = presenterWebRtcEndpoint.processOffer(presenterOffer);

            response.addProperty("result", "accepted");
            response.addProperty("sdpAnswer", answerFromKMS);
            sendMessage(response.toString(), presenter.getSession());

            presenterWebRtcEndpoint.gatherCandidates();
        } else {
            response.addProperty("result", "rejected");
            response.addProperty("reason", "already has presenter");
            sendMessage(response.toString(), session);
        }
    }

    private synchronized void handleViewer(WebSocketSession session, JsonObject message) {
        JsonObject response = new JsonObject();
        response.addProperty("id", "viewerAnswer");

        KurentoUserSession viewer = participants.get(session.getId());
        WebRtcEndpoint viewerWebRtcEndpoint = new WebRtcEndpoint.Builder(pipeline).build();
        viewerWebRtcEndpoint.addIceCandidateFoundListener(event -> {
            JsonObject result = new JsonObject();
            result.addProperty("id", "icecandidate");
            result.add("result", JsonUtils.toJsonObject(event.getCandidate()));
            sendMessage(result.toString(), viewer.getSession());
        });

        viewer.setWebRtcEndpoint(viewerWebRtcEndpoint);
        participants.get(presenterId).getWebRtcEndpoint().connect(viewerWebRtcEndpoint);

        String viewerOffer = message.get("sdpOffer").getAsJsonObject().get("sdp").getAsString();
        String answerFromKMS = viewerWebRtcEndpoint.processOffer(viewerOffer);

        response.addProperty("result", "accepted");
        response.addProperty("sdpAnswer", answerFromKMS);
        sendMessage(response.toString(), viewer.getSession());

        viewerWebRtcEndpoint.gatherCandidates();
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
