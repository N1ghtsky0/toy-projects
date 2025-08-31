package xyz.jiwook.chatting.domain.chatroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.jiwook.chatting.domain.chatroom.entity.ChatRoomEntity;

public interface ChatRoomJpaRepo extends JpaRepository<ChatRoomEntity, String> {
}
