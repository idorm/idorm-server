package idorm.idormServer.community.dto.post;

import idorm.idormServer.community.domain.Post;
import idorm.idormServer.matchingInfo.domain.DormCategory;
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
@ApiModel(value = "Post 요약 응답")
public class PostAbstractResponseDto {

    @ApiModelProperty(position = 1, value="게시글 식별자")
    private Long postId;

    @ApiModelProperty(position = 2, value="멤버 식별자")
    private Long memberId;

    @ApiModelProperty(position = 3, value = "기숙사 분류", example = "DORM1", allowableValues = "DORM1, DORM2, DORM3")
    private DormCategory dormCategory;

    @ApiModelProperty(position = 4, value = "게시글 제목")
    private String title;

    @ApiModelProperty(position = 5, value = "게시글 내용")
    private String content;

    @ApiModelProperty(position = 6, value = "닉네임", example = "null(탈퇴), 익명, 응철이")
    private String nickname;

    @ApiModelProperty(position = 7, value = "익명 여부", example = "null(탈퇴), 익명, 응철이")
    private Boolean isAnonymous;

    @ApiModelProperty(position = 8, value = "공감 수")
    private int likesCount;

    @ApiModelProperty(position = 9, value = "댓글 수")
    private int commentsCount;

    @ApiModelProperty(position = 10, value = "이미지 수")
    private int imagesCount;

    @ApiModelProperty(position = 11, value = "생성일자")
    private LocalDateTime createdAt;

    @ApiModelProperty(position = 12, value = "수정일자")
    private LocalDateTime updatedAt;


    public PostAbstractResponseDto(Post post) {
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
            this.likesCount = post.getPostLikedMembersIsDeletedIsFalse().size();

        if (post.getCommentsIsDeletedIsFalse() != null)
            this.commentsCount = post.getCommentsIsDeletedIsFalse().size();

        if (post.getPostPhotosIsDeletedIsFalse() != null)
            this.imagesCount = post.getPostPhotosIsDeletedIsFalse().size();
    }
}
