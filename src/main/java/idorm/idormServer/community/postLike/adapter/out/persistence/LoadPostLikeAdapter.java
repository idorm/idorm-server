package idorm.idormServer.community.postLike.adapter.out.persistence;

import java.util.Collections;
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

	private final PostLikeRepository postLikeRepository;

	@Override
	public PostLike findByMemberIdAndPostId(Long memberId, Long postId) {
		return postLikeRepository.findByMemberIdAndPostId(memberId, postId)
			.orElseThrow(() -> new NotFoundPostLikeException());

	}

	@Override
	public List<PostLike> findByMemberId(Long memberId) {
		List<PostLike> responses = postLikeRepository.findByMemberId(memberId);
		return responses != null ? responses : Collections.emptyList();
	}

	@Override
	public boolean existsByMemberIdAndPostId(Long memberId, Long postId) {
		return postLikeRepository.existsByMemberIdAndPostId(memberId, postId);
	}

	@Override
	public void validateExists(Member member, Post post) {
		if (existsByMemberIdAndPostId(member.getId(), post.getId())) {
			throw new DuplicatedPostLikeException();
		}
	}
}