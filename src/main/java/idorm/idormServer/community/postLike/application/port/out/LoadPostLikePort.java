package idorm.idormServer.community.postLike.application.port.out;

import java.util.List;

import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.community.postLike.entity.PostLike;
import idorm.idormServer.member.entity.Member;

public interface LoadPostLikePort {

	PostLike findByMemberIdAndPostId(Long memberId, Long postId);

	List<PostLike> findAllByPost(Post post);

	boolean existsByMemberIdAndPostId(Long memberId, Long postId);

	void validateExists(Member member, Post post);
}
