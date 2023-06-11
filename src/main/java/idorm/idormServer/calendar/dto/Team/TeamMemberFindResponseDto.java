package idorm.idormServer.calendar.dto.Team;

import idorm.idormServer.member.domain.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ApiModel(value = "팀 회원 조회 응답")
public class TeamMemberFindResponseDto {

    @ApiModelProperty(position = 1, required = true, value= "회원 등록 순서", example = "1")
    private int order;
    @ApiModelProperty(position = 2, required = true, value= "회원 식별자", example = "10")
    private Long memberId;

    @ApiModelProperty(position = 3, required = true, value = "닉네임", example = "도미")
    private String nickname;

    @ApiModelProperty(position = 4, value = "프로필사진 주소", example = "사진 url")
    private String profilePhotoUrl;

    public TeamMemberFindResponseDto(Member member) { // 탈퇴 회원인지, 팀에 소속되어있는지 체크 후 호출하기
        this.order = member.getTeamOrder();
        this.memberId = member.getId();
        this.nickname = member.getNickname();

        if (member.getMemberPhoto() != null)
            this.profilePhotoUrl = member.getMemberPhoto().getPhotoUrl();
    }
}
