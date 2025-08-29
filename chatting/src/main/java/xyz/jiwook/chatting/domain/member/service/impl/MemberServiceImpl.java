package xyz.jiwook.chatting.domain.member.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.jiwook.chatting.domain.member.entity.MemberEntity;
import xyz.jiwook.chatting.domain.member.repository.MemberCrudRepo;
import xyz.jiwook.chatting.domain.member.service.MemberService;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {
    private final MemberCrudRepo memberCrudRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean isUsernameExist(String username) {
        return memberCrudRepo.existsByUsername(username);
    }

    @Override
    public void createMember(final String username, final String password) {
        memberCrudRepo.save(MemberEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .nickname(username)
                .build());
    }
}
