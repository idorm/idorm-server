package idorm.idormServer.community.dto.comment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@ApiModel(value = "Comment 저장 요청")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class CommentDefaultRequestDto {

    @NotBlank(message = "내용을 입력해 주세요.")
    @ApiModelProperty(position = 1, required = true, dataType = "String", value = "내용", example = "내용내용")
    private String content;

    @ApiModelProperty(position = 2, required = true, dataType = "Boolean", value = "익명 여부", example = "true")
    private Boolean isAnonymous;

    public CommentDefaultRequestDto(String content, Boolean isAnonymous) {
        this.content = content;
        this.isAnonymous = isAnonymous;
    }
}
