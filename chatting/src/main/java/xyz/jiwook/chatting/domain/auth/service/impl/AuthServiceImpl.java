package xyz.jiwook.chatting.domain.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.jiwook.chatting.domain.auth.service.AuthService;
import xyz.jiwook.chatting.domain.auth.vo.RegisterVO;
import xyz.jiwook.chatting.domain.member.service.MemberService;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final MemberService memberService;

    @Override
    public void register(RegisterVO vo) {
        if (vo.getUsername().isEmpty()) {
            throw new IllegalArgumentException("아이디는 필수값입니다.");
        } else if (vo.getPassword().isEmpty()) {
            throw new IllegalArgumentException("비밀번호는 필수값입니다.");
        } else if (!vo.getPassword().equals(vo.getPasswordConfirm())) {
            throw new IllegalArgumentException("입력한 비밀번호가 다릅니다.");
        } else if (memberService.isUsernameExist(vo.getUsername())) {
            throw new IllegalArgumentException("사용불가능한 아이디입니다.");
        }
        memberService.createMember(vo.getUsername(), vo.getPassword());
    }
}
