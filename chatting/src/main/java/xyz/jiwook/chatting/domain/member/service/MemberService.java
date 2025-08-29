package xyz.jiwook.chatting.domain.member.service;

public interface MemberService {
    boolean isUsernameExist(String username);
    void createMember(String username, String password);
}
