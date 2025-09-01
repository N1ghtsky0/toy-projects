package xyz.jiwook.chatting.domain.chat.vo;

import lombok.Data;

@Data
public class ChatMessage {
    public enum MessageType {
        ENTER, LEAVE, CHAT, FILE
    }

    private MessageType type;
    private String message;
    private String roomId;
    private String senderId;
}
