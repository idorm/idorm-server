package idorm.idormServer.community.dto;

import idorm.idormServer.community.domain.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(title = "Comment 커스텀 부모 댓글 및 대댓글들 응답")
public class ParentCommentResponse {

    @Schema(description= "댓글 식별자", required = true)
    private Long commentId;

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

    @Schema( description = "댓글 내용", required = true)
    private String content;

    @Schema(description = "생성일자", required = true)
    private LocalDateTime createdAt;

    @Schema(description = "익명여부")
    private Boolean isAnonymous;

    @Schema(description = "게시글 식별자")
    private Long postId;

    @Schema(description = "대댓글들")
    private List<CommentResponse> subComments = new ArrayList<>();

    public ParentCommentResponse(String anonymousNickname,
                                 Comment parentComment,
                                 List<CommentResponse> subComments) {

        this.memberId = parentComment.getMember().getId();
        this.commentId = parentComment.getId();
        this.postId = parentComment.getPost().getId();
        this.isDeleted = parentComment.getIsDeleted();
        this.content = parentComment.getContent();
        this.createdAt = parentComment.getCreatedAt();
        this.isAnonymous = parentComment.getIsAnonymous();

        if (parentComment.getPost().getIsDeleted())
            this.postId = null;

        if (parentComment.getMember().getIsDeleted()) {
            this.memberId = null;
            this.nickname = null;
        } else if(parentComment.getIsAnonymous()) {
            this.nickname = anonymousNickname;
        } else if(!parentComment.getIsAnonymous()) {
            this.nickname = parentComment.getMember().getNickname();
            if(parentComment.getMember().getMemberPhoto() != null) {
                this.profileUrl = parentComment.getMember().getMemberPhoto().getPhotoUrl();
            }
        }

        if (subComments != null) {
            for (CommentResponse subComment : subComments) {
                this.subComments.add(subComment);
            }
        }
    }
}
