package idorm.idormServer.community.postLike.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Component;

import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.community.postLike.adapter.out.exception.DuplicatedPostLikeException;
import idorm.idormServer.community.postLike.adapter.out.exception.NotFoundPostLikeException;
import idorm.idormServer.community.postLike.application.port.out.LoadPostLikePort;
import idorm.idormServer.community.postLike.entity.PostLike;
import idorm.idormServer.member.entity.Member;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LoadPostLikeAdapter implements LoadPostLikePort {

	private final PostLikeMemberRepository postLikeMemberRepository;

	@Override
	public PostLike findByMemberIdAndPostId(Long memberId, Long postId) {
		return postLikeMemberRepository.findByMemberIdAndPostId(memberId, postId)
			.orElseThrow(() -> new NotFoundPostLikeException());

	}

	@Override
	public List<PostLike> findAllByPost(Post post) {
		return postLikeMemberRepository.findAllByPost(post);
	}

	@Override
	public boolean existsByMemberIdAndPostId(Long memberId, Long postId) {
		return postLikeMemberRepository.existsByMemberIdAndPostId(memberId, postId);
	}

	@Override
	public void validateExists(Member member, Post post) {
		if (existsByMemberIdAndPostId(member.getId(), post.getId())) {
			throw new DuplicatedPostLikeException();
		}
	}
}
