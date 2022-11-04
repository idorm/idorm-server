package idorm.idormServer.community.dto.comment;

import idorm.idormServer.community.domain.Comment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel(value = "Comment 응답")
public class CommentDefaultResponseDto {

    @ApiModelProperty(position = 1, value="댓글 식별자")
    private Long commentId;

    @ApiModelProperty(position = 2, value="게시글 식별자")
    private Long postId;

    @ApiModelProperty(position = 3, value="작성자 식별자")
    private Long memberId;

    @ApiModelProperty(position = 4, value = "댓글 내용")
    private String content;

    @ApiModelProperty(position = 5, value = "댓글 익명 여부")
    private Boolean isAnonymous;

    @ApiModelProperty(position = 6, value = "댓글 삭제 여부")
    private Boolean isVisible;

    public CommentDefaultResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.postId = comment.getPost().getId();
        this.memberId = comment.getMember().getId();
        this.content = comment.getContent();
        this.isAnonymous = comment.getIsAnonymous();
        this.isVisible = comment.getIsVisible();
    }
}
