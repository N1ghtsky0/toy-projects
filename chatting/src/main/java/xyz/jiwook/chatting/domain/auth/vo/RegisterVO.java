package xyz.jiwook.chatting.domain.auth.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterVO extends AuthRequestVO {
    private String passwordConfirm;
}
