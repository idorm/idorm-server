package idorm.idormServer.community.post.adapter.out.persistence;

import static idorm.idormServer.community.post.entity.QPost.post;

import idorm.idormServer.community.post.adapter.out.exception.NotFoundPostException;
import idorm.idormServer.community.post.application.port.in.dto.PostListResponse;
import idorm.idormServer.community.post.application.port.out.LoadPostPort;
import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.matchingInfo.entity.DormCategory;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LoadPostAdapter implements LoadPostPort {

  private final PostRepository postRepository;

  @Override
  public Post findById(Long postId) {
    return postRepository.findById(postId).orElseThrow(() -> new NotFoundPostException());
  }

  @Override
  public Post findByIdAndMemberId(Long postId, Long memberId) {
    Post response = postRepository.findByIdAndMemberId(postId, memberId);

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
  public Page<PostListResponse> findPostsByDormCategoryAndIsDeletedFalse(DormCategory dormCategory, Pageable pageable) {
    return postRepository.findPostsByDormCategoryAndIsDeletedFalse(dormCategory, pageable);
  }

  @Override
  public List<PostListResponse> findTopPostsByDormCateogry(DormCategory dormCategory) {
    List<PostListResponse> responses = postRepository.findTopPostsByDormCateogry(dormCategory);
    return responses.isEmpty() ? new ArrayList<>() : responses;
  }

  @Override
  public Post findTopPostByDormCategory(DormCategory dormCategory) {
    return postRepository.findTopPostByDormCategory(dormCategory);
  }

  @Override
  public List<PostListResponse> findPostsByMemberId(Long memberId) {
    List<PostListResponse> responses = postRepository.findPostsByMemberId(memberId);
    return responses.isEmpty() ? new ArrayList<>() : responses;
  }
}
