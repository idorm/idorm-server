package idorm.idormServer.community.post.adapter.out.persistence;

import static idorm.idormServer.community.comment.entity.QComment.comment;
import static idorm.idormServer.community.post.entity.QPost.post;
import static idorm.idormServer.member.entity.QMember.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import idorm.idormServer.community.post.adapter.out.exception.NotFoundPostException;
import idorm.idormServer.community.post.application.port.out.LoadPostPort;
import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.matchingInfo.entity.DormCategory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

  @Override
  public Post findById(Long postId) {
    Post response = postRepository.findById(postId).orElseThrow(() -> new NotFoundPostException());
    return response;
  }

  @Override
  public Page<Post> findPostsByDormCategoryAndIsDeletedFalse(DormCategory dormCategory, Pageable pageable) {
    List<Post> responses = queryFactory
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

    return new PageImpl<>(responses, pageable, total);
  }

  @Override
  public List<Post> findTopPostsByDormCateogry(DormCategory dormCategory) {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime oneWeekAgo = now.minusWeeks(1);

    List<Post> responses = queryFactory
        .selectFrom(post)
        .where(post.createdAt.between(
                oneWeekAgo.plusHours(9),
                now.plusHours(9))
            .and(post.dormCategory.eq(dormCategory)))
        .fetch();

    return responses.isEmpty() ? new ArrayList<>() : responses;
  }

  @Override
  public List<Post> findPostsByMemberId(Long memberId) {
    List<Post> responses = queryFactory
        .select(post)
        .from(post)
        .join(post.member, member)
        .where(member.id.eq(memberId)
            .and(post.isDeleted.eq(false)))
        .orderBy(post.updatedAt.desc())
        .fetch();

    return responses.isEmpty() ? new ArrayList<>() : responses;
  }

}
