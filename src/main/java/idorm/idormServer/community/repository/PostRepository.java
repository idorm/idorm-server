package idorm.idormServer.community.repository;

import idorm.idormServer.community.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "SELECT post_id " +
            "FROM post p " +
            "WHERE p.dorm_num = :dormNum AND " +
            "p.is_visible = 1", nativeQuery = true)
    List<Long> findManyByDormCategory(@Param("dormNum") String dormNum);
}
