package xyz.jiwook.chatting.domain.chatroom.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.jiwook.chatting.domain.chatroom.entity.ChatRoomEntity;
import xyz.jiwook.chatting.domain.chatroom.repository.ChatRoomJpaRepo;
import xyz.jiwook.chatting.domain.chatroom.service.ChatRoomService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatRoomServiceImpl implements ChatRoomService {
    private final ChatRoomJpaRepo chatRoomJpaRepo;

    @Override
    public List<ChatRoomEntity> findAllChatRoom() {
        return chatRoomJpaRepo.findAll();
    }
}
