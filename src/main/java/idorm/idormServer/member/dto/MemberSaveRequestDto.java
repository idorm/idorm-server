package idorm.idormServer.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@ApiModel(value = "Member 저장 요청")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class MemberSaveRequestDto {

    @Email
    @NotBlank(message = "이메일 입력은 필수입니다.")
    @ApiModelProperty(position = 1, required = true, dataType = "String", value = "이메일", example = "aaa@inu.ac.kr")
    private String email;

    @NotBlank(message = "비밀번호 입력은 필수입니다.")
    @ApiModelProperty(position = 2, required = true, dataType = "String", value = "비밀번호", example = "aaa")
    private String password;

    @NotBlank(message = "닉네임 입력은 필수입니다.")
    @ApiModelProperty(position = 3, required = true, dataType = "String", value = "닉네임", example = "응철이")
    private String nickname;
}