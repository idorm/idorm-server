package idorm.idormServer.community.dto.comment;

import idorm.idormServer.community.domain.Comment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ApiModel(value = "Comment 응답")
public class CommentDefaultResponseDto {

    @ApiModelProperty(position = 1, value="댓글 삭제 여부")
    private Boolean isDeleted;

    @ApiModelProperty(position = 1, value="댓글 식별자")
    private Long commentId;

    @ApiModelProperty(position = 2, value="부모 댓글 식별자")
    private Long parentCommentId;

    @ApiModelProperty(position = 5, value = "댓글 내용")
    private String content;

    @ApiModelProperty(position = 6, value = "닉네임", example = "null, anonymous, 응철이")
    private String nickname;

    @ApiModelProperty(position = 8, value = "생성일자")
    private LocalDateTime createdAt;


    public CommentDefaultResponseDto(Comment comment) {

        this.isDeleted = comment.getIsDeleted();

        this.commentId = comment.getId();
        this.parentCommentId = comment.getParentCommentId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();

        if (comment.getMember() == null) {
            this.nickname = null;
        }

        if(comment.getIsAnonymous() == true) {
            this.nickname = "anonymous";
        }

        if(comment.getIsAnonymous() == false) {
            this.nickname = comment.getMember().getNickname();
        }
    }
}
