package idorm.idormServer.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@ApiModel(value = "Member 프로필 사진 삭제 요청")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class MemberDeletePhotoRequestDto {

    @NotBlank(message = "파일이름 입력은 필수입니다.")
    @ApiModelProperty(position = 1, required = true, dataType = "String", value = "aaa.png")
    private String fileName;
}
