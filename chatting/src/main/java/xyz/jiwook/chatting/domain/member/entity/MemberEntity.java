package xyz.jiwook.chatting.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "member")
public class MemberEntity {
    @Id
    private String username;
    @Column(nullable = false)
    private String password;
    private String nickname;
}
