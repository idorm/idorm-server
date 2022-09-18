package idorm.idormServer.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@ApiModel(value = "Member 정보 수정 - 관리자 계정")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class MemberUpdateStatusAdminRequestDto {

    @NotBlank(message = "닉네임 입력은 필수입니다.")
    @ApiModelProperty(position = 1, required = true, dataType = "String", value = "닉네임", example = "현")
    private String nickname;

    @NotBlank(message = "비밀번호 입력은 필수입니다.")
    @ApiModelProperty(position = 2, required = true, dataType = "String", value = "비밀번호", example = "aaa")
    private String password;
}
