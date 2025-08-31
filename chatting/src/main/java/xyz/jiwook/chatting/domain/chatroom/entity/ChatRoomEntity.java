package xyz.jiwook.chatting.domain.chatroom.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity(name = "chat_room")
public class ChatRoomEntity {
    @Id
    private String id;
    private String name;
    private String description;
    private boolean isPrivate;
    private String password;
    private int maxParticipants;
}
