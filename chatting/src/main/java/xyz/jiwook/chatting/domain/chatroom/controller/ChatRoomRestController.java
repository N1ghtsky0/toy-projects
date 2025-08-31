package xyz.jiwook.chatting.domain.chatroom.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.jiwook.chatting.domain.chatroom.service.ChatRoomService;
import xyz.jiwook.chatting.global.common.controller.CommonController;

import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/v1/chat-rooms")
@RestController
public class ChatRoomRestController extends CommonController {
    private final ChatRoomService chatRoomService;

    @GetMapping("/{roomId}/check")
    public ResponseEntity<?> checkChatRoomAvailable(@PathVariable String roomId, HttpServletRequest request) {
        Map<String, Object> result = chatRoomService.isChatRoomAvailable(roomId);
        result.put("_csrf", getCsrfToken(request));
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/{roomId}/verify-password")
    public ResponseEntity<?> verifyRoomPassword(@PathVariable String roomId, @RequestBody Map<String, Object> reqeustMap, HttpServletRequest request) {
        final String password = reqeustMap.getOrDefault("password", "").toString();
        return ResponseEntity.ok().body(Map.of("result", chatRoomService.isChatRoomPasswordCorrect(roomId, password), "_csrf", getCsrfToken(request)));
    }
}
