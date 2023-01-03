package idorm.idormServer.community.dto.comment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@ApiModel(value = "Comment 저장 요청")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommentDefaultRequestDto {

    @NotBlank(message = "댓글 내용을 입력 해주세요.")
    @ApiModelProperty(position = 1, required = true, dataType = "String", value = "내용", example = "댓글내용")
    private String content;

    @NotNull(message = "댓글 익명 여부를 입력 해주세요.")
    @ApiModelProperty(position = 2, required = true, dataType = "Boolean", value = "익명 여부", example = "true")
    private Boolean isAnonymous;

    @ApiModelProperty(position = 3, required = false, dataType = "Long", value = "대댓글일 시 부모 댓글 식별자", example = "null")
    private Long parentCommentId;
}
