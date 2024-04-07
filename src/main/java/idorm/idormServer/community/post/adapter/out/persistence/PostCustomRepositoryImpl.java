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
        .select(post)
        .from(post)
        .join(post.member, member)
        .where(post.id.eq(postId)
            .and(member.id.eq(memberId)))
        .fetchOne();
  }

  @Override
  public Page<Post> findPostsByDormCategoryAndIsDeletedFalse(DormCategory dormCategory, Pageable pageable) {
    List<Long> ids = queryFactory
        .select(post.id)
        .from(post)
        .where(eqDorm(dormCategory)
            .and(post.isDeleted.eq(false)))
        .orderBy(post.createdAt.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    List<Post> posts = queryFactory
        .selectFrom(post)
        .where(post.id.in(ids))
        .orderBy(post.createdAt.desc())
        .fetch();
    return new PageImpl<>(posts, pageable, ids.size());
  }


  @Override
  public List<Post> findTopPostsByDormCateogry(DormCategory dormCategory) {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime oneWeekAgo = now.minusWeeks(1);

    return queryFactory
        .selectFrom(post)
        .where(post.createdAt.between(
                oneWeekAgo.plusHours(9),
                now.plusHours(9))
            .and(eqDorm(dormCategory))
            .and(post.isDeleted.eq(false)))
        .fetch();
  }

  @Override
  public Post findTopPostByDormCategory(DormCategory dormCategory) {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime oneWeekAgo = now.minusWeeks(1);

    return queryFactory
        .selectFrom(post)
        .where(post.createdAt.between(
                oneWeekAgo.plusHours(9),
                now.plusHours(9))
            .and(eqDorm(dormCategory))
            .and(post.isDeleted.eq(false)))
        .limit(1)
        .fetchOne();
  }

  @Override
  public List<Post> findPostsByMemberId(Long memberId) {
    List<Long> ids = queryFactory
        .select(post.id)
        .from(post)
        .join(post.member, member)
        .where(member.id.eq(memberId)
            .and(post.isDeleted.eq(false)))
        .orderBy(post.updatedAt.desc())
        .fetch();

    return queryFactory
        .select(post)
        .from(post)
        .where(post.id.in(ids))
        .orderBy(post.updatedAt.desc())
        .fetch();
  }

  private BooleanExpression eqDorm(DormCategory dorm) {
    return dorm != null ? post.dormCategory.eq(dorm) : null;
  }
}