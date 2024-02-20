package idorm.idormServer.community.postPhoto.application.port.out;

import idorm.idormServer.community.postPhoto.domain.PostPhoto;
import java.util.List;

public interface LoadPostPhotoPort {

  List<PostPhoto> findByPostId(Long postId);
  PostPhoto findByIdAndPostId(Long photoId,Long postId);
}
