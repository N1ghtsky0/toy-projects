package xyz.jiwook.chatting.domain.auth.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AuthRequestVO {
    private String username;
    private String password;
}
