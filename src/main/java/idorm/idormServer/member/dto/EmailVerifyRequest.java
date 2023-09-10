package idorm.idormServer.member.dto;

import idorm.idormServer.common.ValidationSequence;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@GroupSequence({EmailVerifyRequest.class,
        ValidationSequence.NotBlank.class
})
@Schema(title = "Email 인증코드 확인 요청")
public class EmailVerifyRequest {

    @Schema(required = true, description = "인증코드", example = "111-111")
    @NotBlank(message = "인증코드를 입력 해주세요.", groups = ValidationSequence.NotBlank.class)
    private String code;
}