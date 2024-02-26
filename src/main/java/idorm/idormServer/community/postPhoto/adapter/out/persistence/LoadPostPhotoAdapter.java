package idorm.idormServer.community.postPhoto.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Component;

import idorm.idormServer.community.postPhoto.adapter.out.exception.NotFoundPostPhotoException;
import idorm.idormServer.community.postPhoto.application.port.out.LoadPostPhotoPort;
import idorm.idormServer.community.postPhoto.entity.PostPhoto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LoadPostPhotoAdapter implements LoadPostPhotoPort {

  private final PostPhotoRepository postPhotoRepository;

  @Override
  public List<PostPhoto> findByPostId(Long postId) {
    List<PostPhoto> responses = postPhotoRepository.findAllByPostId(
        postId);
    return responses;
  }

  @Override
  public PostPhoto findByIdAndPostId(Long photoId, Long postId) {
    PostPhoto response = postPhotoRepository.findByIdAndPostId(photoId, postId)
        .orElseThrow(NotFoundPostPhotoException::new);
    return response;
  }
}
