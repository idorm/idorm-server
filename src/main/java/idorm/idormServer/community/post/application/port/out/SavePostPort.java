package idorm.idormServer.community.post.application.port.out;

import idorm.idormServer.community.post.domain.Post;

public interface SavePostPort {
	void save(Post post);
}
