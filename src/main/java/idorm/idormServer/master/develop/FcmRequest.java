package idorm.idormServer.master.develop;

import idorm.idormServer.notification.domain.NotifyType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(title = "개발용 fcm 요청")
public class FcmRequest {

    @Schema(required = true, description = "회원 식별자", example = "3")
    private Long memberId;

    @Schema(required = true, description = "알람 종류",
            allowableValues = "COMMENT, SUBCOMMENT, TOPPOST", example = "TOPPOST")
    private NotifyType alertType;

    @Schema(required = true, description = "게시글 식별자", example = "1")
    private Long contentId;

    @Schema(required = true, description = "대상 토큰")
    private String targetToken;

    @Schema(required = true, description = "제목", example = "어제 1 기숙사의 인기 게시글 입니다.")
    private String title;

    @Schema(required = true, description = "내용", example = "우우우우우웅")
    private String body;
}
