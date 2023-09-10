package idorm.idormServer.member.dto;

import idorm.idormServer.common.ValidationSequence;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.GroupSequence;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@GroupSequence({LoginRequest.class,
        ValidationSequence.NotBlank.class,
        ValidationSequence.Email.class
})
@Schema(title = "Member 로그인 요청")
public class LoginRequest {

    @Schema(required = true, description = "이메일", example = "test1@inu.ac.kr")
    @NotBlank(message = "이메일 입력은 필수입니다.", groups = ValidationSequence.NotBlank.class)
    @Email(message = "이메일 형식이 올바르지 않습니다.", groups = ValidationSequence.Email.class)
    private String email;

    @Schema(required = true, description = "비밀번호", example = "idorm2023!")
    @NotBlank(message = "비밀번호 입력은 필수입니다.", groups = ValidationSequence.NotBlank.class)
    private String password;
}
