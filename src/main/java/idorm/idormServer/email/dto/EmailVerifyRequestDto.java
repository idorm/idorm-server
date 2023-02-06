package idorm.idormServer.email.dto;

import idorm.idormServer.common.ValidationSequence;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@GroupSequence({EmailVerifyRequestDto.class,
        ValidationSequence.NotBlank.class
})
@ApiModel(value = "Email 인증코드 확인 요청")
public class EmailVerifyRequestDto {

    @ApiModelProperty(position = 1, required = true, value = "인증코드", example = "111-111")
    @NotBlank(message = "인증코드를 입력 해주세요.", groups = ValidationSequence.NotBlank.class)
    private String code;
}