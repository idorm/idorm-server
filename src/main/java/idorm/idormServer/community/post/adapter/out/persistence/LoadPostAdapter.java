package idorm.idormServer.community.post.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import idorm.idormServer.community.exception.NotFoundPostException;
import idorm.idormServer.community.post.application.port.out.LoadPostPort;
import idorm.idormServer.community.post.domain.Post;
import idorm.idormServer.matchingInfo.domain.DormCategory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LoadPostAdapter implements LoadPostPort {

  private final PostMapper postMapper;
  private final PostRepository postRepository;

  @Override
  public Post findById(Long postId) {
    PostJpaEntity postJpaEntity = postRepository.findById(postId)
        .orElseThrow(() -> new NotFoundPostException());

    return postMapper.toDomain(postJpaEntity);
  }

  @Override
  public Post findByIdAndIsDeletedIsFalse(Long postId) {
    PostJpaEntity postJpaEntity = postRepository.findByIdAndIsDeletedIsFalse(postId);

    return postMapper.toDomain(postJpaEntity);
  }

  @Override
  public Page<Post> findPostsByDormCategoryAndIsDeletedFalse(DormCategory dormCategory,
      Pageable pageable) {
    Page<PostJpaEntity> responses = postRepository.findAllByDormCategoryAndIsDeletedIsFalseOrderByCreatedAtDesc(
        dormCategory, pageable);

    return responses.map(response -> postMapper.toDomain(response));
  }

  @Override
  public List<Post> findTopPostsByDormCateogry(DormCategory dormCategory) {
    List<PostJpaEntity> responses = postRepository.findTopPostsByDormCategory(dormCategory);
    return responses.isEmpty() ? null : postMapper.toDomain(responses);
  }

  @Override
  public List<Post> findPostsByMemberId(Long memberId) {
    List<PostJpaEntity> responses = postRepository.findAllByMemberIdAndIsDeletedIsFalseOrderByUpdatedAtDesc(
        memberId);

    return responses.isEmpty() ? null : postMapper.toDomain(responses);
  }

  @Override
  public List<Post> findLikePostsByMemberId(Long memberId) {
    List<PostJpaEntity> responses = postRepository.findLikePostsByMemberId(memberId);
    return responses.isEmpty() ? null : postMapper.toDomain(responses);
  }
}
