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
    public PostListData {
    }
}