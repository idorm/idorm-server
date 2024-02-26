package idorm.idormServer.community.postPhoto.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.community.postPhoto.application.port.out.SavePostPhotoPort;
import idorm.idormServer.community.postPhoto.entity.PostPhoto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SavePostPhotoAdapter implements SavePostPhotoPort {

  private final PostPhotoRepository postPhotoRepository;

  @Override
  public void save(PostPhoto postPhot) {
    postPhotoRepository.save(postPhot);
  }
}
