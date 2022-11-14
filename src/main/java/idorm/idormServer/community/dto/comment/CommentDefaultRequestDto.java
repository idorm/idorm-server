package idorm.idormServer.community.dto.comment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@ApiModel(value = "Comment 저장 요청")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentDefaultRequestDto {

    @NotBlank(message = "내용을 입력해 주세요.")
    @ApiModelProperty(position = 1, required = true, dataType = "String", value = "내용", example = "내용내용")
    private String content;

    @ApiModelProperty(position = 2, required = true, dataType = "Boolean", value = "익명 여부", example = "true")
    private Boolean isAnonymous;

    @ApiModelProperty(position = 3, required = false, dataType = "Long", value = "대댓글이라면 부모 댓글 식별자", example = "1")
    private Long parentCommentId;

    public CommentDefaultRequestDto(String content, Boolean isAnonymous, Long parentCommentId) {
        this.content = content;
        this.isAnonymous = isAnonymous;
        this.parentCommentId = parentCommentId;
    }
}
