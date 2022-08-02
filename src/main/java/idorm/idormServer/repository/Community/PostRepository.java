package idorm.idormServer.repository.Community;

import idorm.idormServer.domain.Community.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
}
