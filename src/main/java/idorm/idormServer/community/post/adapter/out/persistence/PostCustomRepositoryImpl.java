package idorm.idormServer.community.post.adapter.out.persistence;

import static idorm.idormServer.community.comment.entity.QComment.comment;
import static idorm.idormServer.community.post.entity.QPost.post;
import static idorm.idormServer.member.entity.QMember.member;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import idorm.idormServer.community.post.application.port.in.dto.PostListData;
import idorm.idormServer.community.post.application.port.in.dto.QPostListData;
import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.community.post.entity.QPost;
import idorm.idormServer.matchingInfo.entity.DormCategory;
import idorm.idormServer.member.entity.MemberStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@Slf4j
public class PostCustomRepositoryImpl implements PostCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final String DELETED_MEMBER_ID = "-999";
    private final String ANONYMOUS_MEMBER = "익명";

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
    public Page<PostListData> findPostsByDormCategoryAndIsDeletedFalse(DormCategory dormCategory, Pageable pageable) {
        List<Long> postIds = queryFactory
            .select(post.id)
            .from(post)
            .where(dormitoryPostCond(dormCategory))
            .orderBy(post.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        List<PostListData> posts = queryFactory
            .select(postListDataFrom())
            .from(post)
            .where(post.id.in(postIds))
            .orderBy(post.createdAt.desc())
            .fetch();

        List<Long> total = queryFactory
            .select(post.id)
            .from(post)
            .where(dormitoryPostCond(dormCategory))
            .fetch();
        return new PageImpl<>(posts, pageable, total.size());
    }


    @Override
    public List<PostListData> findTopPostsByDormCateogry(DormCategory dormCategory) {
        List<Long> postIds = queryFactory
            .select(post.id)
            .from(post)
            .where(weeklyTopPostCond(dormCategory))
            .orderBy(post.likeCount.desc(),
                post.createdAt.desc())
            .limit(10)
            .fetch();

        return queryFactory
            .select(postListDataFrom())
            .from(post)
            .where(post.id.in(postIds))
            .orderBy(post.likeCount.desc(),
                post.createdAt.desc())
            .limit(10)
            .fetch();
    }

    @Override
    public PostListData findTopPostByDormCategory(DormCategory dormCategory) {
        return queryFactory
            .select(postListDataFrom())
            .from(post)
            .where(weeklyTopPostCond(dormCategory))
            .orderBy(post.likeCount.desc(),
                post.createdAt.desc())
            .limit(1)
            .fetchOne();
    }

    @Override
    public List<PostListData> findPostsByMemberId(Long memberId) {
        List<Long> postIds = queryFactory
            .select(post.id)
            .from(post)
            .join(post.member, member)
            .where(member.id.eq(memberId),
                isDeletedEq(false))
            .orderBy(post.createdAt.desc())
            .fetch();

        return queryFactory
            .select(Projections.constructor(
                    PostListData.class,
                    post.id.as("postId"),
                    Expressions.asNumber(memberId).as("memberId"),
                    post.dormCategory.stringValue().as("dormCategory"),
                    post.title,
                    post.content,
                    determineNickname(post),
                    post.isAnonymous,
                    post.likeCount,
                    Expressions.as(JPAExpressions
                        .select(comment.count())
                        .from(comment)
                        .where(comment.post.eq(post),
                            comment.isDeleted.eq(false)), "commentsCount"),
                    post.postPhotos.size().as("imagesCount"),
                    post.createdAt,
                    post.updatedAt
                )
            )
            .from(post)
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

    private QPostListData postListDataFrom() {
        return new QPostListData(
            post.id,
            post.member.id,
            post.dormCategory.stringValue(),
            post.title,
            post.content,
            determineNickname(post),
            post.isAnonymous,
            Expressions.as(JPAExpressions
                .select(comment.count())
                .from(comment)
                .where(comment.post.eq(post),
                    comment.isDeleted.eq(false)), "commentsCount"),
            post.likeCount,
            post.postPhotos.size(),
            post.createdAt,
            post.updatedAt
        );
    }

    private StringExpression determineNickname(QPost post) {
        return new CaseBuilder()
            .when(post.member.memberStatus.eq(MemberStatus.DELETED)).then(DELETED_MEMBER_ID)
            .when(post.isAnonymous.eq(true)).then(ANONYMOUS_MEMBER)
            .otherwise(post.member.nickname.value)
            .as("nickname");
    }
}