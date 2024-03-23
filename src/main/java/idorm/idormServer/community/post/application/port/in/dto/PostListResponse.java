package idorm.idormServer.community.post.application.port.in.dto;

import idorm.idormServer.community.post.entity.Post;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostListResponse {

  private Long postId;
  private Long memberId;
  private String dormCategory;
  private String title;
  private String content;
  private String nickname;
  private Boolean isAnonymous;
  private int likesCount;
  private int commentsCount;
  private int imagesCount;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public PostListResponse(final Post post) {
    this.postId = post.getId();
    this.memberId = post.getMember().getId();
    this.dormCategory = post.getDormCategory().name();
    this.title = post.getTitle();
    this.content = post.getContent();
    this.nickname = isAnonymous(post);
    this.isAnonymous = post.getIsAnonymous();
    this.likesCount = post.getLikeCount();
    this.commentsCount = post.getComments().size();
    this.imagesCount = post.getPostPhotos().size();
    this.createdAt = post.getCreatedAt();
    this.updatedAt = post.getUpdatedAt();
  }

  private static String isAnonymous(Post post) {
    if (post.getMember().getMemberStatus().equals("DELETED")) {
      return "-999";
    } else if (post.getIsAnonymous()) {
      return "익명";
    } else {
      return post.getMember().getNickname().getValue();
    }
  }
}
