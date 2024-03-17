package idorm.idormServer.community.comment.adapter.out.persistence;

import static idorm.idormServer.community.comment.entity.QComment.*;
import static idorm.idormServer.community.post.entity.QPost.*;
import static idorm.idormServer.member.entity.QMember.*;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;

import idorm.idormServer.community.comment.entity.Comment;
import idorm.idormServer.community.postLike.adapter.out.exception.NotFoundPostLikeException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Comment findOneByCommentIdAndPostId(Long commentId, Long postId) {
		Comment response = queryFactory
			.select(comment)
			.from(comment)
			.join(comment.post, post)
			.where(post.id.eq(postId)
				.and(comment.id.eq(commentId)))
			.fetchOne();

		if (response == null)
			throw new NotFoundPostLikeException();

		return response;
	}

	@Override
	public List<Comment> findAllByPostId(Long postId) {
		return queryFactory
			.select(comment)
			.from(comment)
			.join(comment.post, post)
			.where(post.id.eq(postId)
				.and(post.isDeleted.eq(false)))
			.orderBy(comment.createdAt.asc())
			.fetch();
	}

	@Override
	public List<Comment> findAllByMemberId(Long memberId) {
		return queryFactory
			.select(comment)
			.from(comment)
			.join(comment.member, member)
			.where(member.id.eq(memberId)
				.and(comment.isDeleted.eq(false)))
			.orderBy(comment.createdAt.desc())
			.fetch();
	}
}
