package idorm.idormServer.community.dto.post;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel(value = "Post 게시글에 공감한 멤버 수 응답")
public class PostLikedMembersCountByPostResponseDto {

    @ApiModelProperty(position = 1, value="게시글 식별자")
    private Long postId;

    @ApiModelProperty(position = 2, value="게시글 공감 수")
    private int likedCounts;

    @Builder
    public PostLikedMembersCountByPostResponseDto(Long postId,
                                                  int likedCounts) {
        this.postId = postId;
        this.likedCounts = likedCounts;
    }
}
