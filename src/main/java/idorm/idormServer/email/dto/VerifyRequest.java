package idorm.idormServer.email.dto;

import lombok.Getter;

@Getter
public class VerifyRequest {
    private String code;
}