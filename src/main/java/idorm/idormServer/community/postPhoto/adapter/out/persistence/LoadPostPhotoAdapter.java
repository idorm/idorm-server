package idorm.idormServer.community.postPhoto.adapter.out.persistence;

import idorm.idormServer.community.exception.NotFoundPostPhotoException;
import idorm.idormServer.community.postPhoto.application.port.out.LoadPostPhotoPort;
import idorm.idormServer.community.postPhoto.domain.PostPhoto;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LoadPostPhotoAdapter implements LoadPostPhotoPort {

  private final PostPhotoMapper postPhotoMapper;
  private final PostPhotoRepository postPhotoRepository;

  @Override
  public List<PostPhoto> findByPostId(Long postId) {
    List<PostPhotoJpaEntity> responses = postPhotoRepository.findAllByPostId(
        postId);
    return postPhotoMapper.toDomain(responses);
  }

  @Override
  public PostPhoto findByIdAndPostId(Long photoId, Long postId) {
    PostPhotoJpaEntity response = postPhotoRepository.findByIdAndPostId(photoId, postId)
        .orElseThrow(NotFoundPostPhotoException::new);
    return postPhotoMapper.toDomain(response);
  }
}
