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

    @ApiModelProperty(position = 3, value= "댓글 삭제 여부", required = true)
    private Boolean isDeleted;

    @ApiModelProperty(position = 4, value = "닉네임", allowableValues = "null(탈퇴한 회원), anonymous(익명), 응철이(익명 아님)",
            example = "anonymous", required = true)
    private String nickname;

    @ApiModelProperty(position = 5, value = "프로필사진 주소", allowableValues = "null(프로필사진 없음/익명), url(익명 아님)",
            example = "null", required = true)
    private String profileUrl;

    @ApiModelProperty(position = 6, value = "댓글 내용", required = true)
    private String content;

    @ApiModelProperty(position = 7, value = "생성일자", required = true)
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
            if(comment.getMember().getProfilePhoto() != null) {
                this.profileUrl = comment.getMember().getProfilePhoto().getPhotoUrl();
            }
        }
    }
}