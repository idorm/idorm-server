package idorm.idormServer.community.post.adapter.out.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import idorm.idormServer.community.post.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	@Query(value = "SELECT * FROM post WHERE post_id = :id FOR UPDATE", nativeQuery = true)
	Optional<Post> findByPostIdWithLock(@Param("id") Long id);
}
