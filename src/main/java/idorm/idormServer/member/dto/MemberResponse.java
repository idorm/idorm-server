package idorm.idormServer.member.dto;

import idorm.idormServer.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(title = "회원 기본 응답")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberResponse {

    @Schema(required = true, description = "멤버 식별자", example = "1")
    private Long memberId;

    @Schema(required = true, description = "이메일", example = "test1@inu.ac.kr")
    private String email;

    @Schema(description = "닉네임", example = "도미")
    private String nickname;

    @Schema(description = "프로필사진 주소", example = "사진 url")
    private String profilePhotoUrl;

    public MemberResponse(Member member) {
        this.memberId = member.getId();
        this.email = member.getEmail().getEmail();
        this.nickname = member.getNickname();

        if (member.getMemberPhoto() != null)
            this.profilePhotoUrl = member.getMemberPhoto().getPhotoUrl();
    }
}