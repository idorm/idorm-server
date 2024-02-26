package idorm.idormServer.community.post.application.port.out;

import idorm.idormServer.community.post.entity.Post;

public interface SavePostPort {
	void save(Post post);
}
