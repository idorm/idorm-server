package idorm.idormServer.community.comment.adapter.out.persistence;

import static idorm.idormServer.community.comment.entity.QComment.comment;
import static idorm.idormServer.community.post.entity.QPost.post;
import static idorm.idormServer.member.entity.QMember.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import idorm.idormServer.community.comment.entity.Comment;
import idorm.idormServer.community.postLike.adapter.out.exception.NotFoundPostLikeException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentCustomRepositoryImpl implements CommentCustomRepository {

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

        if (response == null) {
            throw new NotFoundPostLikeException();
        }

        return response;
    }

    @Override
    public List<Comment> findAllByPostId(Long postId) {
        List<Long> commentIds = queryFactory
            .select(comment.id)
            .from(comment)
            .join(comment.post, post)
            .where(post.id.eq(postId)
                .and(comment.isDeleted.eq(false)))
            .orderBy(comment.createdAt.desc())
            .fetch();

        return queryFactory
            .select(comment)
            .from(comment)
            .join(comment.post, post)
            .where(post.id.in(commentIds))
            .orderBy(comment.createdAt.desc())
            .fetch();
    }

    @Override
    public List<Comment> findAllByMemberId(Long memberId) {
        List<Long> commentIds = queryFactory
            .select(comment.id)
            .from(comment)
            .join(comment.member, member)
            .where(member.id.eq(memberId)
                .and(comment.isDeleted.eq(false)))
            .orderBy(comment.createdAt.desc())
            .fetch();

        return queryFactory
            .select(comment)
            .from(comment)
            .join(comment.member, member)
            .where(comment.id.in(commentIds))
            .orderBy(comment.createdAt.desc())
            .fetch();
    }
}