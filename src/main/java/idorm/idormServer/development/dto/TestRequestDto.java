package idorm.idormServer.development.dto;

import idorm.idormServer.common.ValidationSequence;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "개발용 fcm 요청")
public class TestRequestDto {

    @ApiModelProperty(position = 1, required = true, value = "대상 토큰")
    private String targetToken;

    @ApiModelProperty(position = 2, required = true, value = "제목")
    private String title;

    @ApiModelProperty(position = 1, required = true, value = "내용")
    private String body;
}
