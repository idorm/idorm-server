package idorm.idormServer.community.dto.comment;

import idorm.idormServer.community.domain.Comment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ApiModel(value = "Comment 커스텀 응답")
public class CommentCustomResponseDto {

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

    @ApiModelProperty(position = 9, value = "대댓글들")
    private List<CommentDefaultResponseDto> subComments = new ArrayList<>();


    public CommentCustomResponseDto(Comment parentComment, List<CommentDefaultResponseDto> subComments) {

        this.isDeleted = parentComment.getIsDeleted();

        this.commentId = parentComment.getId();
        this.parentCommentId = parentComment.getParentCommentId();
        this.content = parentComment.getContent();
        this.createdAt = parentComment.getCreatedAt();

        if (parentComment.getMember() == null) {
            this.nickname = null;
        }

        if(parentComment.getIsAnonymous() == true) {
            this.nickname = "anonymous";
        }

        if(parentComment.getIsAnonymous() == false) {
            this.nickname = parentComment.getMember().getNickname();
        }

        for (CommentDefaultResponseDto subComment : subComments) {
            this.subComments.add(subComment);
        }
    }
}
