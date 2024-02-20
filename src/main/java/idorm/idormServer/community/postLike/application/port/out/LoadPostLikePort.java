package idorm.idormServer.community.postLike.application.port.out;

import idorm.idormServer.community.post.domain.Post;
import idorm.idormServer.community.postLike.domain.PostLike;
import idorm.idormServer.member.domain.Member;

import java.util.List;

public interface LoadPostLikePort {

  PostLike findByMemberIdAndPostId(Long memberId, Long postId);

  List<PostLike> findAllByPost(Post post);

  boolean existsByMemberIdAndPostId(Long memberId, Long postId);

  void validateExists(Member member, Post post);
}
