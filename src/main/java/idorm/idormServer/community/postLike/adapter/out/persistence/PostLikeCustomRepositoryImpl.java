package idorm.idormServer.community.postLike.adapter.out.persistence;

import static idorm.idormServer.community.postLike.entity.QPostLike.postLike;

import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostLikeCustomRepositoryImpl implements PostLikeCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Long> findPostIdsByMemberId(Long memberId) {
		return queryFactory
			.select(postLike.post.id)
			.from(postLike)
			.where(postLike.memberId.eq(memberId))
			.fetch();
	}
}