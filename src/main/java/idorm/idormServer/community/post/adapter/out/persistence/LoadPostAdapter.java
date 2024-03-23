package idorm.idormServer.community.post.adapter.out.persistence;

import static idorm.idormServer.community.comment.entity.QComment.comment;
import static idorm.idormServer.community.post.entity.QPost.post;
import static idorm.idormServer.member.entity.QMember.member;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import idorm.idormServer.community.post.adapter.out.exception.NotFoundPostException;
import idorm.idormServer.community.post.application.port.in.dto.PostListResponse;
import idorm.idormServer.community.post.application.port.out.LoadPostPort;
import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.matchingInfo.entity.DormCategory;
import idorm.idormServer.member.entity.MemberStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LoadPostAdapter implements LoadPostPort {

  private final JPAQueryFactory queryFactory;
  private final PostRepository postRepository;
  private final EntityManager entityManager;

  @Override
  public Post findById(Long postId) {
    return postRepository.findById(postId).orElseThrow(() -> new NotFoundPostException());
  }

  @Override
  public Post findByIdAndMemberId(Long postId, Long memberId) {
    Post response = queryFactory
        .select(post)
        .from(post)
        .join(post.member, member)
        .where(post.id.eq(postId)
            .and(member.id.eq(memberId)))
        .fetchOne();

    if (post == null) {
      throw new NotFoundPostException();
    }

    return response;
  }

  @Override
  public Post findByPostIdWithLock(Long postId) {
    return postRepository.findByPostIdWithLock(postId).orElseThrow(() -> new NotFoundPostException());
  }

  @Override
  public Page<PostListResponse> findPostsByDormCategoryAndIsDeletedFalse(DormCategory dormCategory,
      Pageable pageable) {

    List<PostListResponse> responses = queryFactory
        .select(Projections.constructor(
                PostListResponse.class,
                post.id.as("postId"),
                post.member.id.as("memberId"),
                post.dormCategory.stringValue().as("dormCategory"),
                post.title,
                post.content,
                new CaseBuilder()
                    .when(post.member.memberStatus.eq(MemberStatus.DELETED)).then("-999")
                    .when(post.isAnonymous.eq(true)).then("익명")
                    .otherwise(post.member.nickname.value).as("nickname"),
                post.isAnonymous,
                post.likeCount,
                post.comments.size(),
                post.postPhotos.size().as("imagesCount"),
                post.createdAt,
                post.updatedAt
            )
        )
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

    return new PageImpl<>(responses, pageable, total);
  }

  @Override
  public List<PostListResponse> findTopPostsByDormCateogry(DormCategory dormCategory) {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime oneWeekAgo = now.minusWeeks(1);

    List<PostListResponse> responses = queryFactory
        .select(Projections.constructor(
                PostListResponse.class,
                post.id.as("postId"),
                post.member.id.as("memberId"),
                post.dormCategory.stringValue().as("dormCategory"),
                post.title,
                post.content,
                new CaseBuilder()
                    .when(post.member.memberStatus.eq(MemberStatus.DELETED)).then("-999")
                    .when(post.isAnonymous.eq(true)).then("익명")
                    .otherwise(post.member.nickname.value).as("nickname"),
                post.isAnonymous,
                post.likeCount,
                post.comments.size(),
                post.postPhotos.size().as("imagesCount"),
                post.createdAt,
                post.updatedAt
            )
        )
        .from(post)
        .where(post.createdAt.between(
                oneWeekAgo.plusHours(9),
                now.plusHours(9))
            .and(post.dormCategory.eq(dormCategory)))
        .fetch();

    return responses.isEmpty() ? new ArrayList<>() : responses;
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
  public List<PostListResponse> findPostsByMemberId(Long memberId) {
    List<PostListResponse> responses = queryFactory
        .select(Projections.constructor(
                PostListResponse.class,
                post.id.as("postId"),
                post.member.id.as("memberId"),
                post.dormCategory.stringValue().as("dormCategory"),
                post.title,
                post.content,
                new CaseBuilder()
                    .when(post.member.memberStatus.eq(MemberStatus.DELETED)).then("-999")
                    .when(post.isAnonymous.eq(true)).then("익명")
                    .otherwise(post.member.nickname.value).as("nickname"),
                post.isAnonymous,
                post.likeCount,
                post.comments.size(),
                post.postPhotos.size().as("imagesCount"),
                post.createdAt,
                post.updatedAt
            )
        )
        .from(post)
        .where(post.member.id.eq(memberId))
        .fetch();
    return responses.isEmpty() ? new ArrayList<>() : responses;
  }

}
