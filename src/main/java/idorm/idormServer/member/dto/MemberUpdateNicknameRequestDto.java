package idorm.idormServer.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@ApiModel(value = "Member 닉네임 수정 요청")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberUpdateNicknameRequestDto {

    @ApiModelProperty(position = 1, required = true, value = "닉네임", example = "에러고친응철이")
    @NotBlank(message = "닉네임 입력은 필수입니다.")
    @Size(min = 2, max = 8, message = "닉네임은 2-8자만 가능합니다.")
    @Pattern(regexp = "^[A-Za-z0-9ㄱ-ㅎ가-힣]+$",
            message = "닉네임은 영문, 숫자, 또는 한글의 조합 형식만 가능합니다.")
    private String nickname;
}