package xyz.jiwook.chatting.domain.chatroom.service;

import xyz.jiwook.chatting.domain.chatroom.entity.ChatRoomEntity;
import xyz.jiwook.chatting.domain.chatroom.vo.ChatRoomInfoVO;

import java.util.List;
import java.util.Map;

public interface ChatRoomService {
    List<ChatRoomEntity> findAllChatRoom();
    Map<String, Object> isChatRoomAvailable(String chatRoomId);
    boolean isChatRoomPasswordCorrect(String chatRoomId, String password);
    ChatRoomInfoVO getChatRoomInfo(String roomId);
}
