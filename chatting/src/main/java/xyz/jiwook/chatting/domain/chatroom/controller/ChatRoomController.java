package xyz.jiwook.chatting.domain.chatroom.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.Map;

@Controller
public class ChatRoomController {
    @PostMapping("/chat")
    public String joinChatRoomRedirector(RedirectAttributes redirectAttributes, @RequestParam("roomId") String roomId) {
        redirectAttributes.addFlashAttribute("roomId", roomId);
        return "redirect:/chat";
    }

    @GetMapping("/chat")
    public String displayChatRoomPage(HttpServletRequest request) {
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap == null) {
            return "redirect:/";
        }
        String roomId = flashMap.get("roomId").toString();
        return "chat/room";
    }
}
