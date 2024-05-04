package idorm.idormServer.community.post.adapter.out.persistence;

import idorm.idormServer.community.post.application.port.out.SavePostPort;
import idorm.idormServer.community.post.entity.Post;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SavePostAdapter implements SavePostPort {

	private final PostRepository postRepository;

	@Override
	public void save(Post post) {
		postRepository.save(post);
	}
}