package idorm.idormServer.community.postPhoto.adapter.out.persistence;

import idorm.idormServer.community.postPhoto.adapter.out.exception.NotFoundPostPhotoException;
import idorm.idormServer.community.postPhoto.application.port.out.LoadPostPhotoPort;
import idorm.idormServer.community.postPhoto.entity.PostPhoto;

import java.util.List;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LoadPostPhotoAdapter implements LoadPostPhotoPort {

	private final PostPhotoRepository postPhotoRepository;

	@Override
	public List<PostPhoto> findByPostId(Long postId) {
		List<PostPhoto> responses = postPhotoRepository.findAllByPostId(postId);
		if(responses == null) {
			throw new NotFoundPostPhotoException();
		}
		return responses;
	}
}