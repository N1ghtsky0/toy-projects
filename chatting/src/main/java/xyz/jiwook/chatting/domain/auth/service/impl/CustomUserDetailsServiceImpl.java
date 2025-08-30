package xyz.jiwook.chatting.domain.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import xyz.jiwook.chatting.domain.auth.model.CustomUserDetails;
import xyz.jiwook.chatting.domain.member.entity.MemberEntity;
import xyz.jiwook.chatting.domain.member.repository.MemberCrudRepo;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    private final MemberCrudRepo memberCrudRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberEntity entity = memberCrudRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return new CustomUserDetails(entity);
    }
}
