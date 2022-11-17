package idorm.idormServer.matchingInfo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ApiModel(value = "MatchingInfo 수정 요청")
public class MatchingInfoUpdateIsPublicRequestDto {

    @ApiModelProperty(position = 1, required = true, dataType = "Boolean", example = "true")
    private Boolean isMatchingInfoPublic;
}
