package idorm.idormServer.community.postPhoto.adapter.out.persistence;

import idorm.idormServer.community.postPhoto.application.port.out.SavePostPhotoPort;
import idorm.idormServer.community.postPhoto.domain.PostPhoto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SavePostPhotoAdapter implements SavePostPhotoPort {

  private final PostPhotoMapper postPhotoMapper;
  private final PostPhotoRepository postPhotoRepository;

  @Override
  public void save(PostPhoto postPhoto) {
    PostPhotoJpaEntity postPhotoJpaEntity = postPhotoMapper.toEntity(postPhoto);
    postPhotoRepository.save(postPhotoJpaEntity);
  }
}
