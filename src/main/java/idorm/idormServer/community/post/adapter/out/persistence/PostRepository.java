package idorm.idormServer.community.post.adapter.out.persistence;

import idorm.idormServer.community.post.entity.Post;
import java.util.Optional;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

  @Query(value = "SELECT * FROM post WHERE post_id = :id FOR UPDATE", nativeQuery = true)
  Optional<Post> findByPostIdWithLock(@Param("id") Long id);
}
