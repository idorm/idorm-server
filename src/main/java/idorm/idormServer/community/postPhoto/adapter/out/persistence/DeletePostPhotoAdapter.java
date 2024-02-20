package idorm.idormServer.community.postPhoto.adapter.out.persistence;

import idorm.idormServer.community.postPhoto.application.port.out.DeletePostPhotoPort;
import idorm.idormServer.community.postPhoto.domain.PostPhoto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class DeletePostPhotoAdapter implements DeletePostPhotoPort {

  private final PostPhotoMapper postPhotoMapper;
  private final PostPhotoRepository postPhotoRepository;

  @Override
  public void delete(final PostPhoto postPhoto) {
    postPhotoRepository.delete(postPhotoMapper.toEntity(postPhoto));
  }

  @Override
  public void deleteAllByPostId(final Long postId) {
    postPhotoRepository.deleteAllByPostId(postId);
  }

  @Override
  public void deleteById(final Long postPhotoId) {
    postPhotoRepository.deleteById(postPhotoId);
  }
}
