package xyz.jiwook.chatting.domain.chatroom.vo;

import lombok.Getter;
import xyz.jiwook.chatting.domain.chatroom.entity.ChatRoomEntity;

@Getter
public class ChatRoomInfoVO {
    private final String roomId;
    private final String roomName;
    private final int maxParticipants;

    public ChatRoomInfoVO(ChatRoomEntity entity) {
        this.roomId = entity.getId();
        this.roomName = entity.getName();
        this.maxParticipants = entity.getMaxParticipants();
    }
}
