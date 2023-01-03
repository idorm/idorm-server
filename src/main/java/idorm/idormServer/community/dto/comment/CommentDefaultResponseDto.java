package idorm.idormServer.community.dto.comment;

import idorm.idormServer.community.domain.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "Comment 응답 - 자식 댓글(대댓글) 응답으로 대댓글을 미포함한다.")
public class CommentDefaultResponseDto {

    @Schema(description = "댓글 식별자")
    private Long commentId;

    @Schema(description = "부모 댓글 식별자")
    private Long parentCommentId;

    @Schema(description = "댓글 삭제 여부")
    private Boolean isDeleted;

    @Schema(description = "닉네임", example = "null, anonymous, 응철이")
    private String nickname;

    @Schema(description = "프로필사진 주소", example = "null(사진이 없거나, 익명), url")
    private String profileUrl;

    @Schema(description = "댓글 내용")
    private String content;

    @Schema(description = "생성일자")
    private LocalDateTime createdAt;


    public CommentDefaultResponseDto(Comment comment) {

        this.isDeleted = comment.getIsDeleted();
        this.commentId = comment.getId();
        this.parentCommentId = comment.getParentCommentId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();

        if (comment.getMember() == null) {
            this.nickname = null;
        } else if(comment.getIsAnonymous() == true) {
            this.nickname = "anonymous";
        } else if(comment.getIsAnonymous() == false) {
            this.nickname = comment.getMember().getNickname();
            if(comment.getMember().getPhoto() != null) {
                this.profileUrl = comment.getMember().getPhoto().getUrl();
            }
        }
    }
}