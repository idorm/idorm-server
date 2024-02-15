package idorm.idormServer.community.application.port.in.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import idorm.idormServer.community.domain.Post;
import idorm.idormServer.community.domain.PostPhoto;
import idorm.idormServer.matchingInfo.domain.DormCategory;
import idorm.idormServer.member.domain.MemberPhoto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(title = "PostJpaEntity 단건 응답")
public class PostResponse {

    @Schema(description= "게시글 식별자", example = "1")
    private Long postId;

    @Schema(description= "멤버 식별자", example = "2")
    private Long memberId;

    @Schema(description= "기숙사 분류", example = "DORM1", allowableValues = "DORM1, DORM2, DORM3")
    private DormCategory dormCategory;

    @Schema(description= "게시글 제목", example = "제에목")
    private String title;

    @Schema(description= "게시글 내용", example = "내애용")
    private String content;

    @Schema(description= "닉네임", example = "null(탈퇴), 익명, 응철이")
    private String nickname;

    @Schema(description= "프로필사진 주소", example = "null(사진이 없거나, 익명), url")
    private String profileUrl;

    @Schema(description= "공감 수")
    private int likesCount;

    @Schema(description= "댓글 수")
    private int commentsCount;

    @Schema(description= "이미지 수")
    private int imagesCount;

    @Schema(description= "공감 여부", allowableValues = "true(공감), false(공감 안함), null(게시글 단건 " +
            "조회가 아닌 경우)")
    private Boolean isLiked;

    @Schema(description= "생성일자")
    private LocalDateTime createdAt;

    @Schema(description= "수정일자")
    private LocalDateTime updatedAt;

    @Schema(description= "익명여부")
    private Boolean isAnonymous;

    @Schema(description= "게시글 업로드 사진들")
    private List<PostPhotoResponse> postPhotos = new ArrayList<>();

    @Schema(description= "댓글/대댓글 목록")
    private List<ParentCommentResponse> comments = new ArrayList<>();

    // 게시글 저장 시에만 사용
    public PostResponse(Post post, MemberPhoto memberPhoto) {
        this.postId = post.getId();
        this.memberId = post.getMemberEntity().getId();
        this.dormCategory = DormCategory.valueOf(post.getDormCategory());
        this.title = post.getTitleEmbeddedEntity();
        this.content = post.getContent();
        this.isLiked = false;
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.likesCount = 0;
        this.commentsCount = 0;
        this.imagesCount = 0;
        this.isAnonymous = post.getIsAnonymous();

        if(post.getMemberEntity().getIsDeleted()) { // 회원 탈퇴의 경우
            this.nickname = null;
            this.memberId = null;
        } else if(!post.getIsAnonymous()) { // 익명이 아닌 경우
            this.nickname = post.getMemberEntity().getNickname();

            if(memberPhoto != null)
                this.profileUrl = memberPhoto.getPhotoUrl();
        } else if(post.getIsAnonymous()) { // 익명일 경우
            this.nickname = "익명";
        }

        if(post.getPostPhotosIsDeletedIsFalse() != null) {
            List<PostPhoto> postPhotos = post.getPostPhotosIsDeletedIsFalse();
            this.imagesCount = postPhotos.size();

            for (PostPhoto postPhoto : postPhotos)
                this.postPhotos.add(new PostPhotoResponse(postPhoto));
        }
    }

    public PostResponse(Post post, List<ParentCommentResponse> comments, MemberPhoto postMemberPhoto, boolean isLiked) {
        this.postId = post.getId();
        this.memberId = post.getMemberEntity().getId();
        this.dormCategory = DormCategory.valueOf(post.getDormCategory());
        this.title = post.getTitleEmbeddedEntity();
        this.content = post.getContent();
        this.isLiked = isLiked;
        this.likesCount = 0;
        this.commentsCount = 0;
        this.imagesCount = 0;
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.isAnonymous = post.getIsAnonymous();

        if (post.getPostLikedMembersIsDeletedIsFalse() != null)
            this.likesCount = post.getPostLikedMembersCnt();

        if (post.getCommentsIsDeletedIsFalse() != null)
            this.commentsCount = post.getCommentsCount();

        if(post.getMemberEntity().getIsDeleted()) { // 회원 탈퇴의 경우
            this.memberId = null;
            this.nickname = null;
        } else if(!post.getIsAnonymous()) { // 익명이 아닌 경우
            this.nickname = post.getMemberEntity().getNickname();
            if(postMemberPhoto != null) {
                this.profileUrl = postMemberPhoto.getPhotoUrl();
            }
        } else if(post.getIsAnonymous()) { // 익명일 경우
            this.nickname = "익명";
        }

        if(post.getPostPhotosIsDeletedIsFalse() != null) {
            List<PostPhoto> postPhotos = post.getPostPhotosIsDeletedIsFalse();
            this.imagesCount = postPhotos.size();

            for (PostPhoto postPhoto : postPhotos)
                this.postPhotos.add(new PostPhotoResponse(postPhoto));
        }

        if(comments != null) {
            for(ParentCommentResponse comment : comments) {
                this.comments.add(comment);
            }
        }
    }
}