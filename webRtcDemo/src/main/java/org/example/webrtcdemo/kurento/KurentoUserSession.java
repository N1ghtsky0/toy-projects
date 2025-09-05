package org.example.webrtcdemo.kurento;

import lombok.Data;
import org.kurento.client.IceCandidate;
import org.kurento.client.WebRtcEndpoint;
import org.springframework.web.socket.WebSocketSession;

@Data
public class KurentoUserSession {
    private final WebSocketSession session;

    private WebRtcEndpoint webRtcEndpoint;

    public KurentoUserSession(WebSocketSession session) {
        this.session = session;
    }

    public void addCandidate(IceCandidate candidate) {
        webRtcEndpoint.addIceCandidate(candidate);
    }
}
