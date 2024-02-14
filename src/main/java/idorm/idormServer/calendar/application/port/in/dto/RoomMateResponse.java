package idorm.idormServer.calendar.application.port.in.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.domain.MemberPhoto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(title = "팀 회원 조회 응답")
public class RoomMateResponse {

    @Schema(name = "order", description = "회원 등록 순서", format = "int", example = "1")
    @JsonProperty("order")
    private int order;

    @Schema(required = true, name = "memberId", description = "회원 식별자", example = "10")
    @JsonProperty("memberId")
    private Long memberId;

    @Schema(required = true, description = "닉네임", example = "도미")
    @JsonProperty("nickname")
    private String nickname;

    @Schema(description = "프로필사진 주소", example = "사진 url")
    @JsonProperty("profilePhotoUrl")
    private String profilePhotoUrl;

    public RoomMateResponse(Member memberEntity, MemberPhoto memberPhoto) {
        this.order = memberEntity.getTeamOrder();
        this.memberId = memberEntity.getId();
        this.nickname = memberEntity.getNickname();

        if (memberPhoto != null)
            this.profilePhotoUrl = memberPhoto.getPhotoUrl();
    }
}
