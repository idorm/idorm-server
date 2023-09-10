package idorm.idormServer.community.dto;

import idorm.idormServer.community.domain.Post;
import idorm.idormServer.matching.domain.DormCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(title = "Post 요약 응답")
public class PostSummaryResponse {

    @Schema(description="게시글 식별자")
    private Long postId;

    @Schema(description="멤버 식별자")
    private Long memberId;

    @Schema(description= "기숙사 분류", example = "DORM1", allowableValues = "DORM1, DORM2, DORM3")
    private DormCategory dormCategory;

    @Schema(description= "게시글 제목")
    private String title;

    @Schema(description = "게시글 내용")
    private String content;

    @Schema(description= "닉네임", example = "null(탈퇴), 익명, 응철이")
    private String nickname;

    @Schema(description= "익명 여부", example = "null(탈퇴), 익명, 응철이")
    private Boolean isAnonymous;

    @Schema(description= "공감 수")
    private int likesCount;

    @Schema(description= "댓글 수")
    private int commentsCount;

    @Schema(description= "이미지 수")
    private int imagesCount;

    @Schema(description= "생성일자")
    private LocalDateTime createdAt;

    @Schema(description= "수정일자")
    private LocalDateTime updatedAt;


    public PostSummaryResponse(Post post) {
        this.postId = post.getId();
        this.dormCategory = DormCategory.valueOf(post.getDormCategory());
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.isAnonymous = post.getIsAnonymous();

        if (!post.getMember().getIsDeleted()) {
            this.memberId = post.getMember().getId();

            if (post.getIsAnonymous())
                this.nickname = "익명";
            else
                this.nickname = post.getMember().getNickname();
        } else {
            this.nickname = null;
            this.isAnonymous = null;
        }

        if (post.getPostLikedMembersIsDeletedIsFalse() != null)
            this.likesCount = post.getPostLikedMembersCnt();

        if (post.getCommentsIsDeletedIsFalse() != null)
            this.commentsCount = post.getCommentsCount();

        if (post.getPostPhotosIsDeletedIsFalse() != null)
            this.imagesCount = post.getPostPhotosCount();
    }
}
