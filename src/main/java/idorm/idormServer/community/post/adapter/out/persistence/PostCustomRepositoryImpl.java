package idorm.idormServer.community.post.adapter.out.persistence;

import static idorm.idormServer.community.post.entity.QPost.post;
import static idorm.idormServer.member.entity.QMember.member;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.matchingInfo.entity.DormCategory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Post findByIdAndMemberId(Long postId, Long memberId) {
        return queryFactory
            .selectFrom(post)
            .join(post.member, member)
            .where(post.id.eq(postId),
                member.id.eq(memberId))
            .fetchOne();
    }

    @Override
    public Page<Post> findPostsByDormCategoryAndIsDeletedFalse(DormCategory dormCategory, Pageable pageable) {
        List<Long> postIds = queryFactory
            .select(post.id)
            .from(post)
            .where(weeklyTopPostCond(dormCategory))
            .orderBy(post.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        List<Post> posts = queryFactory
            .selectFrom(post)
            .where(post.id.in(postIds))
            .orderBy(post.createdAt.desc())
            .fetch();
        return new PageImpl<>(posts, pageable, postIds.size());
    }


    @Override
    public List<Post> findTopPostsByDormCateogry(DormCategory dormCategory) {
        List<Long> postIds = queryFactory
            .select(post.id)
            .from(post)
            .where(weeklyTopPostCond(dormCategory))
            .orderBy(post.likeCount.desc(), post.createdAt.desc())
            .limit(10)
            .fetch();

        return queryFactory
            .selectFrom(post)
            .where(post.id.in(postIds))
            .orderBy(post.likeCount.desc(), post.createdAt.desc())
            .fetch();
    }

    @Override
    public Post findTopPostByDormCategory(DormCategory dormCategory) {
        return queryFactory
            .selectFrom(post)
            .where(weeklyTopPostCond(dormCategory))
            .orderBy(post.likeCount.desc(), post.createdAt.desc())
            .limit(1)
            .fetchOne();
    }

    @Override
    public List<Post> findPostsByMemberId(Long memberId) {
        List<Long> postIds = queryFactory
            .select(post.id)
            .from(post)
            .join(post.member, member)
            .where(member.id.eq(memberId),
                isDeletedEq(false))
            .orderBy(post.createdAt.desc())
            .fetch();

        return queryFactory
            .selectFrom(post)
            .where(post.id.in(postIds))
            .orderBy(post.createdAt.desc())
            .fetch();
    }

    private BooleanExpression weeklyTopPostCond(DormCategory dormCategory) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneWeekAgo = now.minusWeeks(1);
        LocalDateTime start = oneWeekAgo.plusHours(9);
        LocalDateTime end = now.plusHours(9);
        return post.createdAt.between(start, end)
            .and(dormitoryPostCond(dormCategory));
    }

    private BooleanExpression dormitoryPostCond(DormCategory dormCategory) {
        return post.dormCategory.eq(dormCategory)
            .and(isDeletedEq(false));
    }

    private BooleanExpression isDeletedEq(Boolean isDeleted) {
        return post.isDeleted.eq(isDeleted);
    }
}