package idorm.idormServer.member.dto;

import idorm.idormServer.common.ValidationSequence;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@GroupSequence({MemberUpdateNicknameRequestDto.class,
        ValidationSequence.NotBlank.class,
        ValidationSequence.Size.class,
        ValidationSequence.Pattern.class,
})
@ApiModel(value = "Member 닉네임 수정 요청")
public class MemberUpdateNicknameRequestDto {

    @ApiModelProperty(position = 1, required = true, value = "닉네임", example = "에러고친응철이")
    @NotBlank(message = "닉네임 입력은 필수입니다.", groups = ValidationSequence.NotBlank.class)
    @Size(min = 2, max = 8, message = "닉네임은 2~8자만 가능합니다.", groups = ValidationSequence.Size.class)
    @Pattern(regexp = "^[A-Za-z0-9ㄱ-ㅎ가-힣]+$",
            message = "닉네임은 영문, 숫자, 또는 한글의 조합 형식만 가능합니다.", groups = ValidationSequence.Pattern.class)
    private String nickname;
}