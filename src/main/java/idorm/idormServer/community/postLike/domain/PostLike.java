package idorm.idormServer.community.postLike.domain;

import idorm.idormServer.community.post.domain.Post;
import idorm.idormServer.member.domain.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostLike {

  private Long id;
  private Post post;
  private Member member;

  public PostLike(final Post post, final Member member) {
    this.post = post;
    this.member = member;

    post.addPostLike(this);
  }

  public static PostLike forMapper(final Long id, final Post post, final Member member) {
    return new PostLike(id, post, member);
  }
}