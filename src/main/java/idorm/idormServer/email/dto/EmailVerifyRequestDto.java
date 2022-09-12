package idorm.idormServer.email.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@ApiModel(value = "Email 인증코드 확인 요청")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerifyRequestDto {

    @ApiModelProperty(position = 1, required = true, dataType = "String", value = "인증코드", example = "111-111")
    @NotBlank(message = "인증코드를 입력해주세요.")
    private String code;
}