package idorm.idormServer.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@ApiModel(value = "Member 로그인 상태에서 닉네임 변경 요청")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class MemberUpdateNicknameRequestDto {

    @NotBlank(message = "닉네임 입력은 필수입니다.")
    @ApiModelProperty(position = 1, required = true, dataType = "String", value = "닉네임", example = "종강하고싶은 응철이")
    private String nickname;
}