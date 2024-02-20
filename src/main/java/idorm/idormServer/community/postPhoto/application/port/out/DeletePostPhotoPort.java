package idorm.idormServer.community.postPhoto.application.port.out;

import idorm.idormServer.community.postPhoto.domain.PostPhoto;

public interface DeletePostPhotoPort {
	void delete(PostPhoto postPhoto);

	void deleteAllByPostId(Long postId);

	void deleteById(Long postPhotoId);
}
