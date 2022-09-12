package idorm.idormServer.email.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@ApiModel(value = "Email 인증 요청")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailAuthRequestDto {

    @ApiModelProperty(position = 1, required = true, dataType = "String", value = "이메일", example = "aaa@inu.ac.kr")
    @NotBlank(message = "이메일을 입력해 주세요.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;
}