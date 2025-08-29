package xyz.jiwook.chatting.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.jiwook.chatting.domain.auth.service.AuthService;
import xyz.jiwook.chatting.domain.auth.vo.RegisterVO;
import xyz.jiwook.chatting.domain.member.service.MemberService;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthRestController {
    private final MemberService memberService;
    private final AuthService authService;

    @GetMapping("/available")
    public ResponseEntity<?> checkUsernameAvailable(@RequestParam("username") String username) {
        return ResponseEntity.ok(Map.of("result", !memberService.isUsernameExist(username)));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterVO registerVO) {
        Map<String, Object> responseBody = new HashMap<>();
        try {
            authService.register(registerVO);
            responseBody.put("result", true);
        } catch (IllegalArgumentException e) {
            responseBody.put("result", false);
            responseBody.put("message", e.getMessage());
        } catch (Exception e) {
            responseBody.put("result", false);
            responseBody.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(responseBody);
        }
        return ResponseEntity.ok(responseBody);
    }
}
