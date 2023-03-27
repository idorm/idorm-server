package idorm.idormServer.matchingInfo.dto;

import idorm.idormServer.common.ValidationSequence;
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

    @ApiModelProperty(position = 1, required = true, value = "매칭 이미지 공개 여부", example = "true")
    @NotNull(message = "매칭 이미지 공개 여부를 입력해 주세요.", groups = ValidationSequence.NotNull.class)
    private Boolean isMatchingInfoPublic;
}
