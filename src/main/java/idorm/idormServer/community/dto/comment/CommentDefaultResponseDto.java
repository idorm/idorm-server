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
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel(value = "Comment 댓글 및 대댓글 기본 응답")
public class CommentDefaultResponseDto {

    @ApiModelProperty(position = 1, value= "댓글 식별자", required = true)
    private Long commentId;

    @ApiModelProperty(position = 2, value= "부모 댓글 식별자")
    private Long parentCommentId;

    @ApiModelProperty(position = 3, value = "회원 식별자")
    private Long memberId;

    @ApiModelProperty(position = 4, value= "댓글 삭제 여부", required = true)
    private Boolean isDeleted;

    @ApiModelProperty(position = 5, value = "닉네임", allowableValues = "null(탈퇴한 회원), 익명1, 응철이",
            example = "익명1", required = true)
    private String nickname;

    @ApiModelProperty(position = 6, value = "프로필사진 주소", allowableValues = "null(프로필사진 없음/익명), url(익명 아님)",
            example = "null", required = true)
    private String profileUrl;

    @ApiModelProperty(position = 7, value = "댓글 내용", required = true)
    private String content;

    @ApiModelProperty(position = 8, value = "생성일자", required = true)
    private LocalDateTime createdAt;

    @ApiModelProperty(position = 9, value = "익명여부")
    private Boolean isAnonymous;

    @ApiModelProperty(position = 10, value = "게시글 식별자")
    private Long postId;

    public CommentDefaultResponseDto(Comment comment) {

        this.memberId = comment.getMember().getId();
        this.commentId = comment.getId();
        this.parentCommentId = comment.getParentCommentId();
        this.isDeleted = comment.getIsDeleted();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.isAnonymous = comment.getIsAnonymous();
        this.postId = comment.getPost().getId();

        if (comment.getMember().getIsDeleted()) {
            this.nickname = null;
            this.memberId = null;
        } else if(comment.getIsAnonymous()) {
            this.nickname = "익명";
        } else if(!comment.getIsAnonymous()) {
            this.nickname = comment.getMember().getNickname();
            if(comment.getMember().getMemberPhoto() != null) {
                this.profileUrl = comment.getMember().getMemberPhoto().getPhotoUrl();
            }
        }
    }
    public CommentDefaultResponseDto(String anonymousNickname, Comment comment) {

        this.memberId = comment.getMember().getId();
        this.commentId = comment.getId();
        this.parentCommentId = comment.getParentCommentId();
        this.isDeleted = comment.getIsDeleted();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.isAnonymous = comment.getIsAnonymous();

        if (comment.getMember().getIsDeleted()) {
            this.memberId = null;
            this.nickname = null;
        } else if(comment.getIsAnonymous()) {
            this.nickname = anonymousNickname;
        } else if(!comment.getIsAnonymous()) {
            this.nickname = comment.getMember().getNickname();
            if(comment.getMember().getMemberPhoto() != null) {
                this.profileUrl = comment.getMember().getMemberPhoto().getPhotoUrl();
            }
        }
    }
}