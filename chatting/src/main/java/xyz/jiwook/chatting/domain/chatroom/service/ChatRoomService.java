package xyz.jiwook.chatting.domain.chatroom.service;

import xyz.jiwook.chatting.domain.chatroom.entity.ChatRoomEntity;

import java.util.List;

public interface ChatRoomService {
    List<ChatRoomEntity> findAllChatRoom();
}
