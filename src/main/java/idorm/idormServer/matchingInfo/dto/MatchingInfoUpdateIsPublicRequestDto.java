package idorm.idormServer.matchingInfo.dto;

import idorm.idormServer.common.ValidationSequence;
import idorm.idormServer.member.dto.MemberSaveRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@GroupSequence({MatchingInfoUpdateIsPublicRequestDto.class,
        ValidationSequence.NotNull.class
})
@ApiModel(value = "MatchingInfo 수정 요청")
public class MatchingInfoUpdateIsPublicRequestDto {

    @ApiModelProperty(position = 1, required = true, example = "true")
    @NotNull(message = "매칭이미지 공개 여부 입력은 필수입니다.", groups = ValidationSequence.NotNull.class)
    private boolean isMatchingInfoPublic;
}
