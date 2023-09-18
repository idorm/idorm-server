package idorm.idormServer.member.dto;

import idorm.idormServer.common.ValidationSequence;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@GroupSequence({NicknameRequest.class,
        ValidationSequence.NotBlank.class,
        ValidationSequence.Size.class,
        ValidationSequence.Pattern.class,
})
@Schema(title = "Member 닉네임 수정 요청")
public class NicknameRequest {

    @Schema(required = true, description = "닉네임", example = "에러고친응철이")
    @NotBlank(message = "닉네임 입력은 필수입니다.", groups = ValidationSequence.NotBlank.class)
    @Size(min = 2, max = 8, message = "닉네임은 2~8자 이내여야 합니다.", groups = ValidationSequence.Size.class)
    @Pattern(regexp = "^[A-Za-z0-9ㄱ-ㅎ가-힣]+$",
            message = "닉네임은 영문, 숫자, 또는 한글의 조합 형식만 가능합니다.", groups = ValidationSequence.Pattern.class)
    private String nickname;
}