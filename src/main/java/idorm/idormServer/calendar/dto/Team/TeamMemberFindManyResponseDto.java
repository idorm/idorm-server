package idorm.idormServer.calendar.dto.Team;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel(value = "팀 회원 다건 조회 응답")
public class TeamMemberFindManyResponseDto {

    @ApiModelProperty(position = 1, required = true, value= "팀 식별자", example = "1")
    private Long teamId;

    @ApiModelProperty(position = 2, required = true, value= "팀 폭발 확인 필요 여부", example = "false")
    private Boolean isNeedToConfirmDeleted;

    @ApiModelProperty(position = 3, required = true, value= "팀 회원들")
    private List<TeamMemberFindResponseDto> members = new ArrayList<>();

    public TeamMemberFindManyResponseDto(Long teamId,
                                         Boolean isNeedToConfirmDeleted,
                                         List<TeamMemberFindResponseDto> members) {
        this.teamId = teamId;
        this.isNeedToConfirmDeleted = isNeedToConfirmDeleted;

        if (members != null)
            this.members = members.stream().collect(Collectors.toList());
    }
}
