package xyz.jiwook.chatting.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import xyz.jiwook.chatting.domain.chat.vo.ChatMessage;

@RequiredArgsConstructor
@Controller
public class ChatController {
    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat/enter")
    public void enterChatRoom(ChatMessage message) {
        message.setType(ChatMessage.MessageType.ENTER);
        message.setMessage(String.format("%s님이 입장했습니다.", message.getSenderId()));
        messagingTemplate.convertAndSend("/topic/chat/" + message.getRoomId(), message);
    }
}
