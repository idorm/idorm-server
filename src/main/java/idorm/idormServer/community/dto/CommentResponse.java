package idorm.idormServer.community.dto;

import idorm.idormServer.community.domain.Comment;
import idorm.idormServer.member.domain.MemberPhoto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(title = "Comment 댓글 및 대댓글 기본 응답")
public class CommentResponse {

    @Schema(description= "댓글 식별자", required = true)
    private Long commentId;

    @Schema(description= "부모 댓글 식별자")
    private Long parentCommentId;

    @Schema(description = "회원 식별자")
    private Long memberId;

    @Schema(description= "댓글 삭제 여부", required = true)
    private Boolean isDeleted;

    @Schema(description = "닉네임", allowableValues = "null(탈퇴한 회원), 익명1, 응철이",
            example = "익명1", required = true)
    private String nickname;

    @Schema(description = "프로필사진 주소", allowableValues = "null(프로필사진 없음/익명), url(익명 아님)",
            example = "null", required = true)
    private String profileUrl;

    @Schema(description = "댓글 내용", required = true)
    private String content;

    @Schema(description = "생성일자", required = true)
    private LocalDateTime createdAt;

    @Schema(description = "익명여부")
    private Boolean isAnonymous;

    @Schema(description = "게시글 식별자", example = "1, null(삭제된 게시글)")
    private Long postId;

    public CommentResponse(Comment comment, MemberPhoto commentMemberPhoto) {

        this.memberId = comment.getMember().getId();
        this.commentId = comment.getId();
        this.parentCommentId = comment.getParentCommentId();
        this.postId = comment.getPost().getId();
        this.isDeleted = comment.getIsDeleted();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.isAnonymous = comment.getIsAnonymous();

        if (comment.getPost().getIsDeleted())
            this.postId = null;

        if (comment.getMember().getIsDeleted()) {
            this.nickname = null;
            this.memberId = null;
        } else if(comment.getIsAnonymous()) {
            this.nickname = "익명";
        } else if(!comment.getIsAnonymous()) {
            this.nickname = comment.getMember().getNickname();
            if(commentMemberPhoto != null) {
                this.profileUrl = commentMemberPhoto.getPhotoUrl();
            }
        }
    }
    public CommentResponse(String anonymousNickname, Comment comment, MemberPhoto commentMemberPhoto) {

        this.memberId = comment.getMember().getId();
        this.commentId = comment.getId();
        this.parentCommentId = comment.getParentCommentId();
        this.postId = comment.getPost().getId();
        this.isDeleted = comment.getIsDeleted();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.isAnonymous = comment.getIsAnonymous();

        if (comment.getPost().getIsDeleted())
            this.postId = null;

        if (comment.getMember().getIsDeleted()) {
            this.memberId = null;
            this.nickname = null;
        } else if(comment.getIsAnonymous()) {
            this.nickname = anonymousNickname;
        } else if(!comment.getIsAnonymous()) {
            this.nickname = comment.getMember().getNickname();
            if(commentMemberPhoto != null) {
                this.profileUrl = commentMemberPhoto.getPhotoUrl();
            }
        }
    }
}