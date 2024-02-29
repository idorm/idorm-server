package idorm.idormServer.member.application.port.in.dto;

import javax.validation.constraints.NotBlank;

public record NicknameUpdateRequest(
//        @NotBlank(message = "닉네임은 공백일 수 없습니다.")
        String nickname
) {
}
