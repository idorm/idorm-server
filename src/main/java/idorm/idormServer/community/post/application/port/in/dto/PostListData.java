package idorm.idormServer.community.post.application.port.in.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

public record PostListData(
    Long postId,
    Long memberId,
    String dormCategory,
    String title,
    String content,
    String nickname,
    Boolean isAnonymous,
    Long commentsCount,
    int likesCount,
    int imagesCount,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    @QueryProjection
    public PostListData(Long postId,
        Long memberId,
        String dormCategory,
        String title,
        String content,
        String nickname,
        Boolean isAnonymous,
        Long commentsCount,
        int likesCount,
        int imagesCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
        this.postId = postId;
        this.memberId = memberId;
        this.dormCategory = dormCategory;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.isAnonymous = isAnonymous;
        this.commentsCount = commentsCount;
        this.likesCount = likesCount;
        this.imagesCount = imagesCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
