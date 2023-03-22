package idorm.idormServer.member.dto;

import idorm.idormServer.member.domain.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@ApiModel(value = "Member 기본 응답")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberDefaultResponseDto {

    @ApiModelProperty(position = 1, required = true, value = "멤버 식별자", example = "1")
    private Long memberId;

    @ApiModelProperty(position = 2, required = true, value = "이메일", example = "test1@inu.ac.kr")
    private String email;

    @ApiModelProperty(position = 3, value = "닉네임", example = "도미")
    private String nickname;

    @ApiModelProperty(position = 5, value = "프로필사진 주소", example = "사진 url")
    private String profilePhotoUrl;

    public MemberDefaultResponseDto(Member member) {
        this.memberId = member.getId();
        this.email = member.getEmail().getEmail();
        this.nickname = member.getNickname();

        if (member.getMemberPhoto() != null)
            this.profilePhotoUrl = member.getMemberPhoto().getPhotoUrl();
    }
}