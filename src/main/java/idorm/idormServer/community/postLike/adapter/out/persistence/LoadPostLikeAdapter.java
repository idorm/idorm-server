package idorm.idormServer.community.postLike.adapter.out.persistence;

import static idorm.idormServer.community.post.entity.QPost.post;
import static idorm.idormServer.community.postLike.entity.QPostLike.postLike;

import com.querydsl.jpa.impl.JPAQueryFactory;
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

	private final JPAQueryFactory queryFactory;
	private final PostLikeMemberRepository postLikeMemberRepository;

	@Override
	public PostLike findByMemberIdAndPostId(Long memberId, Long postId) {
		return postLikeMemberRepository.findByMemberIdAndPostId(memberId, postId)
			.orElseThrow(() -> new NotFoundPostLikeException());

	}

	@Override
	public List<PostLike> findByMemberId(Long memberId) {
		return queryFactory.select(postLike)
				.from(postLike)
				.join(postLike.post, post)
				.where(post.isDeleted.eq(false)
						.and(postLike.memberId.eq(memberId)))
				.orderBy(postLike.id.desc())
				.fetch();
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
