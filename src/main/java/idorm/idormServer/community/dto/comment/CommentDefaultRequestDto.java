package idorm.idormServer.community.dto.comment;

import idorm.idormServer.common.ValidationSequence;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@GroupSequence({CommentDefaultRequestDto.class,
        ValidationSequence.NotBlank.class,
        ValidationSequence.NotNull.class
})
@ApiModel(value = "Comment 기본 요청")
public class CommentDefaultRequestDto {

    @ApiModelProperty(position = 1, required = true, value = "내용", example = "댓글내용")
    @NotBlank(message = "댓글 내용을 입력 해주세요.", groups = ValidationSequence.NotBlank.class)
    private String content;

    @ApiModelProperty(position = 2, required = true, value = "익명 여부", example = "true")
    @NotNull(message = "익명 여부를 입력 해주세요.", groups = ValidationSequence.NotNull.class)
    private Boolean isAnonymous;

    @ApiModelProperty(position = 3, value = "대댓글일 시 부모 댓글 식별자", example = "null")
    private Long parentCommentId;
}
