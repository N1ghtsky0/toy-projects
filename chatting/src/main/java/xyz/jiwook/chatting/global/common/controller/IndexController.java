package xyz.jiwook.chatting.global.common.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import xyz.jiwook.chatting.domain.chatroom.service.ChatRoomService;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final ChatRoomService chatRoomService;

    @GetMapping("/")
    public String displayIndexPage(Model model) {
        model.addAttribute("chatRooms", chatRoomService.findAllChatRoom());
        return "index";
    }
}
