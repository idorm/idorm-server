package idorm.idormServer.community.comment.adapter.out.persistence;

import static idorm.idormServer.community.comment.entity.QComment.*;
import static idorm.idormServer.community.post.entity.QPost.*;
import static idorm.idormServer.member.entity.QMember.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.querydsl.jpa.impl.JPAQueryFactory;

import idorm.idormServer.community.comment.adapter.out.exception.NotFoundCommentException;
import idorm.idormServer.community.comment.application.port.out.LoadCommentPort;
import idorm.idormServer.community.comment.entity.Comment;
import idorm.idormServer.community.postLike.adapter.out.exception.NotFoundPostLikeException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LoadCommentAdapter implements LoadCommentPort {

	private final JPAQueryFactory queryFactory;
	private final CommentRepository commentRepository;

	@Override
	public Comment findById(Long commentId) {
		Comment response = commentRepository.findByIdAndIsDeletedIsFalse(commentId)
			.orElseThrow(NotFoundCommentException::new);
		return response;
	}

	@Override
	public Comment findOneByCommentIdAndPostId(Long commentId, Long postId) {
		Comment response = queryFactory
			.select(comment)
			.from(comment)
			.join(comment.post, post)
			.where(post.id.eq(postId)
				.and(comment.id.eq(commentId)))
			.fetchOne();

		if (response == null) throw new NotFoundPostLikeException();

			return response;

	}

	@Override
	public List<Comment> findAllByPostId(Long postId) {
		List<Comment> responses = queryFactory
			.select(comment)
			.from(comment)
			.join(comment.post, post)
			.where(post.id.eq(postId)
				.and(post.isDeleted.eq(false)))
			.orderBy(comment.createdAt.asc())
			.fetch();
		return responses.isEmpty() ?  new ArrayList<>() : responses;
	}

	@Override
	public List<Comment> findAllByMemberId(Long memberId) {
		List<Comment> responses = queryFactory
			.select(comment)
			.from(comment)
			.join(comment.member, member)
			.where(member.id.eq(memberId)
				.and(comment.isDeleted.eq(false)))
			.orderBy(comment.createdAt.desc())
			.fetch();
		return responses.isEmpty() ? new ArrayList<>() : responses;
	}
}
