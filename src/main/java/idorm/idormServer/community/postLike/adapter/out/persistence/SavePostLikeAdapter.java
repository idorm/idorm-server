package idorm.idormServer.community.postLike.adapter.out.persistence;

import idorm.idormServer.community.postLike.application.port.out.SavePostLikePort;
import idorm.idormServer.community.postLike.entity.PostLike;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SavePostLikeAdapter implements SavePostLikePort {

	private final PostLikeRepository postLikeRepository;

	@Override
	public void save(PostLike postLike) {
		postLikeRepository.save(postLike);
	}
}