package idorm.idormServer.calendar.dto.Team;

import idorm.idormServer.member.domain.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ApiModel(value = "팀 회원 조회 응답 - 당일 외박 여부 포함")
public class TeamMemberFullResponseDto {

    @ApiModelProperty(position = 1, required = true, value= "회원 등록 순서", example = "1")
    private int order;

    @ApiModelProperty(position = 2, required = true, value= "회원 식별자", example = "10")
    private Long memberId;

    @ApiModelProperty(position = 3, required = true, value = "닉네임", example = "도미")
    private String nickname;

    @ApiModelProperty(position = 4, value = "프로필사진 주소", example = "사진 url")
    private String profilePhotoUrl;

    @ApiModelProperty(position = 5, value = "당일 외박 여부", example = "false")
    private Boolean sleepoverYn;

    @Builder
    public TeamMemberFullResponseDto(Member member, Boolean sleepoverYn) {
        this.order = member.getTeamOrder();
        this.memberId = member.getId();
        this.nickname = member.getNickname();
        this.sleepoverYn = sleepoverYn;

        if (member.getMemberPhoto() != null)
            this.profilePhotoUrl = member.getMemberPhoto().getPhotoUrl();
    }
}
