package idorm.idormServer.community.post.adapter.out.persistence;

import static idorm.idormServer.community.comment.entity.QComment.*;
import static idorm.idormServer.community.post.entity.QPost.*;
import static idorm.idormServer.member.entity.QMember.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.jpa.impl.JPAQueryFactory;

import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.matchingInfo.entity.DormCategory;
import lombok.RequiredArgsConstructor;

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
    List<Post> posts = queryFactory
        .select(post)
        .from(post)
        .join(post.comments, comment).on(comment.isDeleted.eq(false))
        .where(post.dormCategory.eq(dormCategory)
            .and(comment.isDeleted.eq(false))
            .and(post.isDeleted.eq(false)))
        .offset(pageable.getOffset())
        .orderBy(post.createdAt.desc())
        .limit(pageable.getPageSize())
        .fetch();

    long total = queryFactory
        .select(post)
        .from(post)
        .where(post.dormCategory.eq(dormCategory)
            .and(post.isDeleted.eq(false)))
        .fetchCount();

    return new PageImpl<>(posts, pageable, total);
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
            .and(post.dormCategory.eq(dormCategory)))
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
            .and(post.dormCategory.eq(dormCategory)))
        .limit(1)
        .fetchOne();
  }

  @Override
  public List<Post> findPostsByMemberId(Long memberId) {
    return queryFactory
        .select(post)
        .from(post)
        .join(post.member, member)
        .where(member.id.eq(memberId)
            .and(post.isDeleted.eq(false)))
        .orderBy(post.updatedAt.desc())
        .fetch();
  }
}