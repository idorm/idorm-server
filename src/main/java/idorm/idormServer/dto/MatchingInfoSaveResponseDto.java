package idorm.idormServer.dto;

import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel(value = "온보딩정보 생성 요청")
public class MatchingInfoSaveResponseDto {
}
