package xyz.jiwook.chatting.domain.chatroom.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.jiwook.chatting.domain.chatroom.entity.ChatRoomEntity;
import xyz.jiwook.chatting.domain.chatroom.repository.ChatRoomJpaRepo;
import xyz.jiwook.chatting.domain.chatroom.service.ChatRoomService;
import xyz.jiwook.chatting.domain.chatroom.vo.ChatRoomInfoVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ChatRoomServiceImpl implements ChatRoomService {
    private final ChatRoomJpaRepo chatRoomJpaRepo;

    @Override
    public List<ChatRoomEntity> findAllChatRoom() {
        return chatRoomJpaRepo.findAll();
    }

    @Override
    public Map<String, Object> isChatRoomAvailable(String chatRoomId) {
        Map<String, Object> result = new HashMap<>();
        chatRoomJpaRepo.findById(chatRoomId).ifPresentOrElse((chatRoomEntity -> {
            result.put("result", true);
            result.put("private", chatRoomEntity.isPrivate());
        }), () -> result.put("result", false));
        return result;
    }

    @Override
    public boolean isChatRoomPasswordCorrect(String chatRoomId, String password) {
        return chatRoomJpaRepo.findById(chatRoomId)
                .map(chatRoomEntity -> chatRoomEntity.getPassword().equals(password))
                .orElse(false);
    }

    @Override
    public ChatRoomInfoVO getChatRoomInfo(String roomId) {
        return chatRoomJpaRepo.findById(roomId)
                .map(ChatRoomInfoVO::new)
                .orElse(null);
    }
}
