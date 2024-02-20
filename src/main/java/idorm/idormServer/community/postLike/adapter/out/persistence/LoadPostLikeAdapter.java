package idorm.idormServer.community.postLike.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Component;

import idorm.idormServer.community.exception.DuplicatedPostLikeException;
import idorm.idormServer.community.exception.NotFoundPostLikeException;
import idorm.idormServer.community.post.adapter.out.persistence.PostMapper;
import idorm.idormServer.community.post.domain.Post;
import idorm.idormServer.community.postLike.application.port.out.LoadPostLikePort;
import idorm.idormServer.community.postLike.domain.PostLike;
import idorm.idormServer.member.domain.Member;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LoadPostLikeAdapter implements LoadPostLikePort {

	private final PostLikeMapper postLikeMapper;
	private final PostMapper postMapper;
	private final PostLikeMemberRepository postLikeMemberRepository;

	@Override
	public PostLike findByMemberIdAndPostId(Long memberId, Long postId) {
		PostLikeJpaEntity response = postLikeMemberRepository.findByMemberIdAndPostId(memberId, postId)
			.orElseThrow(() -> new NotFoundPostLikeException());

		return postLikeMapper.toDomain(response);

	}

	@Override
	public List<PostLike> findAllByPost(Post post) {
		List<PostLikeJpaEntity> responses = postLikeMemberRepository.findAllByPostAndIsDeletedIsFalse(
			postMapper.toEntity(post));
		return postLikeMapper.toDomain(responses);
	}

	@Override
	public boolean existsByMemberIdAndPostId(Long memberId, Long postId) {
		return postLikeMemberRepository.existsByMemberIdAndPostIdAndIsDeletedIsFalse(memberId, postId);
	}

	@Override
	public void validateExists(Member member, Post post) {
		if (existsByMemberIdAndPostId(member.getId(), post.getId())) {
			throw new DuplicatedPostLikeException();
		}
	}
}
