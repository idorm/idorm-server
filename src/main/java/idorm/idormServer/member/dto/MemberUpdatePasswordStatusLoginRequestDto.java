package idorm.idormServer.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@ApiModel(value = "Member 로그인 상태에서 비밀번호 변경 요청")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class MemberUpdatePasswordStatusLoginRequestDto {

    @NotBlank(message = "비밀번호 입력은 필수입니다.")
    @ApiModelProperty(position = 1, required = true, dataType = "String", value = "비밀번호", example = "aaaaaaa7!")
    private String password;
}
