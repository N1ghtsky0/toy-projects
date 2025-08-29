package xyz.jiwook.chatting.domain.auth.service;

import xyz.jiwook.chatting.domain.auth.vo.RegisterVO;

public interface AuthService {
    void register(RegisterVO vo);
}
