package idorm.idormServer.matching.dto;

import idorm.idormServer.member.domain.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel(value = "Matching 응답")
@AllArgsConstructor
@Builder
public class MatchingDefaultResponseDto {

    @ApiModelProperty(position = 1, value="멤버 식별자")
    private Long memberId;

    public MatchingDefaultResponseDto(Member member) {
        this.memberId = member.getId();
    }
}
