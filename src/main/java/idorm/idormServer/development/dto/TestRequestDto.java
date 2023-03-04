package idorm.idormServer.development.dto;

import idorm.idormServer.fcm.domain.NotifyType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "개발용 fcm 요청")
public class TestRequestDto {

    @ApiModelProperty(position = 1, required = true, value = "알람 종류",
            allowableValues = "COMMENT, SUBCOMMENT, TOPPOST", example = "TOPPOST")
    private NotifyType alertType;

    @ApiModelProperty(position = 1, required = true, value = "게시글 식별자", example = "1")
    private Long contentId;

    @ApiModelProperty(position = 1, required = true, value = "대상 토큰",
    example = "f2uPzBhiTm-peoTQdXOzF1:APA91bEzJTtxjy7AeMaY_rCJ9jKSC2Br4-209FnNM3HuuYAOGf6KvMpWY-S5ahHHza-U5BaR40gd2dh" +
            "Ut91DjR1IZFJTGweQ57jB04AxVaytkbs-pChiItDrIFJQmNS6dYpMl0T291TS")
    private String targetToken;

    @ApiModelProperty(position = 2, required = true, value = "제목", example = "어제 1 기숙사의 인기 게시글 입니다.")
    private String title;

    @ApiModelProperty(position = 1, required = true, value = "내용", example = "우우우우우웅")
    private String body;
}
