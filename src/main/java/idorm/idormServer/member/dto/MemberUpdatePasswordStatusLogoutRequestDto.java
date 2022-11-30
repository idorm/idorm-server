package idorm.idormServer.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@ApiModel(value = "Member 로그아웃 상태에서 비밀번호 변경 요청")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class MemberUpdatePasswordStatusLogoutRequestDto {

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일 입력은 필수입니다.")
    @ApiModelProperty(position = 1, required = true, dataType = "String", value = "이메일", example = "aaa@inu.ac.kr")
    private String email;

    @NotBlank(message = "비밀번호 입력은 필수입니다.")
    @ApiModelProperty(position = 2, required = true, dataType = "String", value = "비밀번호", example = "aaa")
    private String password;
}
