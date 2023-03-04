package idorm.idormServer.development.dto;

import idorm.idormServer.fcm.domain.FcmChannel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "개발용 fcm 요청")
public class TestRequestDto {

    @ApiModelProperty(position = 1, required = true, value = "알람 종류", allowableValues = "COMMENT, SUBCOMMENT, TOPPOST")
    private FcmChannel alertType;

    @ApiModelProperty(position = 1, required = true, value = "대상 토큰")
    private String targetToken;

    @ApiModelProperty(position = 2, required = true, value = "제목")
    private String title;

    @ApiModelProperty(position = 1, required = true, value = "내용")
    private String body;
}
