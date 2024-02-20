package idorm.idormServer.community.postPhoto.application;

import java.util.List;

import org.springframework.stereotype.Service;

import idorm.idormServer.community.post.domain.Post;
import idorm.idormServer.community.postPhoto.application.port.in.PostPhotoUseCase;
import idorm.idormServer.community.postPhoto.application.port.out.DeletePostPhotoPort;
import idorm.idormServer.community.postPhoto.application.port.out.LoadPostPhotoPort;
import idorm.idormServer.community.postPhoto.application.port.out.SavePostPhotoPort;
import idorm.idormServer.community.postPhoto.domain.PostPhoto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostPhotoService implements PostPhotoUseCase {

	private final SavePostPhotoPort savePostPhotoPort;
	private final DeletePostPhotoPort deletePostPhotoPort;
	private final LoadPostPhotoPort loadPostPhotoPort;

	@Override
	public void save(final Post post, final List<String> pohotoUrls) {
		final List<PostPhoto> postPhotos = PostPhoto.of(post, pohotoUrls);
		postPhotos.forEach(postPhoto -> savePostPhotoPort.save(postPhoto));
	}

	@Override
	public void delete(final List<PostPhoto> postPhotos) {
		postPhotos.forEach(postPhoto -> deletePostPhotoPort.deleteById(postPhoto.getId()));
	}

	@Override
	public List<PostPhoto> findAllByPost(final Post post) {
		return loadPostPhotoPort.findByPostId(post.getId());
	}
}