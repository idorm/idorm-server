package idorm.idormServer.community.postPhoto.application.port.out;

import java.util.List;

import idorm.idormServer.community.postPhoto.entity.PostPhoto;

public interface LoadPostPhotoPort {

  List<PostPhoto> findByPostId(Long postId);
  PostPhoto findByIdAndPostId(Long photoId,Long postId);
}
