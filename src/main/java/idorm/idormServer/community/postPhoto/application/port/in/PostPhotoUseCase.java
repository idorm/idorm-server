package idorm.idormServer.community.postPhoto.application.port.in;

import java.util.List;

import idorm.idormServer.community.post.domain.Post;
import idorm.idormServer.community.postPhoto.domain.PostPhoto;

public interface PostPhotoUseCase {

	void save(Post post, List<String> photoUrls);

	void delete(List<PostPhoto> postPhotos);

	List<PostPhoto> findAllByPost(Post post);
}
