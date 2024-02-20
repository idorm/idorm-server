package idorm.idormServer.community.postPhoto.application.port.out;

import idorm.idormServer.community.postPhoto.domain.PostPhoto;

public interface SavePostPhotoPort {

  void save(PostPhoto postPhoto);
}
