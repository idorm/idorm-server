package idorm.idormServer.matching.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel(value = "Matching 호불호 룸메이트 식별자 요청")
@AllArgsConstructor
@Builder
public class MatchingSelectedMemberIdRequestDto {

    @NotNull(message = "멤버 식별자는 필수입니다.")
    @ApiModelProperty(position = 1, required = true, dataType = "Long", value = "호불호 멤버 식별자", example = "2")
    private Long selectedMemberId;
}
