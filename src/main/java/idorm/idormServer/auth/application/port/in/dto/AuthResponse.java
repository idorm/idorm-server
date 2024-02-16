package idorm.idormServer.auth.application.port.in.dto;

import lombok.Getter;

@Getter
public class AuthResponse {
    private Long id;
    private String role;
    private String nickname;

    public AuthResponse(Long id, String role, String nickname) {
        this.id = id;
        this.role = role;
        this.nickname = nickname;
    }
}