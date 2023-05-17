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
@GroupSequence({MemberUpdateStatusAdminRequestDto.class,
        ValidationSequence.NotBlank.class,
        ValidationSequence.Email.class,
        ValidationSequence.Size.class,
        ValidationSequence.Pattern.class,
})
@ApiModel(value = "[관리자 계정] Member 정보 수정")
public class MemberUpdateStatusAdminRequestDto {

    @ApiModelProperty(position = 1, required = true, value = "닉네임", example = "탈주한응철이")
    @NotBlank(message = "닉네임 입력은 필수입니다.", groups = ValidationSequence.NotBlank.class)
    @Size(min = 2, max = 8, message = "닉네임은 2~8자 이내여야 합니다.", groups = ValidationSequence.Size.class)
    @Pattern(regexp = "^[A-Za-z0-9ㄱ-ㅎ가-힣]+$",
            message = "닉네임은 영문, 숫자, 또는 한글의 조합 형식만 가능합니다.", groups = ValidationSequence.Pattern.class)
    private String nickname;

    @ApiModelProperty(position = 2, required = true, value = "비밀번호", example = "aaaaaaa7!")
    @NotBlank(message = "비밀번호 입력은 필수입니다.", groups = ValidationSequence.NotBlank.class)
    @Size(min = 8, max = 15, message = "비밀번호는 8~15자 이내여야 합니다.", groups = ValidationSequence.Size.class)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,15}$",
            message = "비밀번호는 영소문자, 숫자, 특수문자는 필수이고 대문자는 선택인 형식만 가능합니다.",
            groups = ValidationSequence.Pattern.class)
    private String password;
}
