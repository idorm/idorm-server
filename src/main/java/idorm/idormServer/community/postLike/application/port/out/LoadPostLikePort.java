package idorm.idormServer.community.postLike.application.port.out;

import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.community.postLike.entity.PostLike;
import idorm.idormServer.member.entity.Member;
import java.util.List;

public interface LoadPostLikePort {

  PostLike findByMemberIdAndPostId(Long memberId, Long postId);

  List<PostLike> findByMemberId(Long memberId);

  boolean existsByMemberIdAndPostId(Long memberId, Long postId);

  void validateExists(Member member, Post post);
}
