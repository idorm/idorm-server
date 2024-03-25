package idorm.idormServer.community.postLike.adapter.out.persistence;

import static idorm.idormServer.community.post.entity.QPost.*;
import static idorm.idormServer.community.postLike.entity.QPostLike.*;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;

import idorm.idormServer.community.postLike.entity.PostLike;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostLikeCustomRepositoryImpl implements PostLikeCustomRepository {

	private final JPAQueryFactory queryFactory;

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
}