package xyz.jiwook.chatting.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import xyz.jiwook.chatting.domain.auth.service.AuthService;
import xyz.jiwook.chatting.domain.member.service.MemberService;

@RequiredArgsConstructor
@Controller
public class AuthController {
    private final MemberService memberService;
    private final AuthService authService;

    @GetMapping("/register")
    public String displayRegisterPage() {
        return "auth/register";
    }
}
