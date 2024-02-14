package idorm.idormServer.calendar.application.port.in.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(title = "팀 회원 다건 조회 응답 - 당일 외박 여부 포함")
public class RoomMatesFullResponse {

    @Schema(required = true, description= "팀 식별자", example = "1")
    private Long teamId;

    @Schema(required = true, description= "팀 폭발 확인 필요 여부", example = "false")
    private Boolean isNeedToConfirmDeleted;

    @Schema(required = true, description= "팀 회원들")
    private List<RoomMateFullResponse> members = new ArrayList<>();

    @Builder
    public RoomMatesFullResponse(Long teamId,
                                 Boolean isNeedToConfirmDeleted,
                                 List<RoomMateFullResponse> members) {
        this.teamId = teamId;
        this.isNeedToConfirmDeleted = isNeedToConfirmDeleted;

        if (members != null)
            this.members = members.stream().collect(Collectors.toList());
    }
}
