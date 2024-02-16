package idorm.idormServer.calendar.application.port.in.dto;

import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.domain.MemberPhoto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(title = "팀 회원 조회 응답 - 당일 외박 여부 포함")
public class RoomMateFullResponse {

    @Schema(required = true, description= "회원 등록 순서", example = "1")
    private int order;

    @Schema(required = true, description= "회원 식별자", example = "10")
    private Long memberId;

    @Schema(required = true, description = "닉네임", example = "도미")
    private String nickname;

    @Schema(description = "프로필사진 주소", example = "사진 url")
    private String profilePhotoUrl;

    @Schema(description = "당일 외박 여부", example = "false")
    private Boolean sleepoverYn;

    @Builder
    public RoomMateFullResponse(Member memberEntity, Boolean sleepoverYn, MemberPhoto memberPhoto) {
        this.order = memberEntity.getTeamOrder();
        this.memberId = memberEntity.getId();
        this.nickname = memberEntity.getNickname();
        this.sleepoverYn = sleepoverYn;

        if (memberPhoto != null)
            this.profilePhotoUrl = memberPhoto.getPhotoUrl();
    }
}