package idorm.idormServer.community.postPhoto.application.port.in;

import java.util.List;

import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.community.postPhoto.entity.PostPhoto;

public interface PostPhotoUseCase {

	void save(Post post, List<String> photoUrls);

	void delete(List<PostPhoto> postPhotoDomains);

	List<PostPhoto> findAllByPost(Post post);
}
