package idorm.idormServer.community.comment.adapter.out.persistence;

import idorm.idormServer.community.comment.entity.Comment;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentCustomRepository {

	Optional<Comment> findByIdAndIsDeletedIsFalse(Long id);
}