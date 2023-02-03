package idorm.idormServer.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@ApiModel(value = "Member 로그인 요청")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberLoginRequestDto {

    @ApiModelProperty(position = 1, required = true, value = "이메일", example = "aaa@inu.ac.kr")
    @NotBlank(message = "이메일 입력은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @ApiModelProperty(position = 2, required = true, value = "비밀번호", example = "aaaaaaa7!")
    @NotBlank(message = "비밀번호 입력은 필수입니다.")
    private String password;
}
