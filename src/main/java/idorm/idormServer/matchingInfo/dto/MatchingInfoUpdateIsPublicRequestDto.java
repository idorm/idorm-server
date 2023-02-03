package idorm.idormServer.matchingInfo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ApiModel(value = "MatchingInfo 수정 요청")
public class MatchingInfoUpdateIsPublicRequestDto {

    @NotNull(message = "매칭이미지 공개 여부 입력은 필수입니다.")
    @ApiModelProperty(position = 1, required = true, dataType = "boolean", example = "true")
    private Boolean isMatchingInfoPublic;
}
