package idorm.idormServer.photo.repository;

import idorm.idormServer.photo.domain.PostPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostPhotoRepository extends JpaRepository<PostPhoto, Long> {

    Optional<PostPhoto> findByIdAndPostIdAndIsDeletedFalse(Long id, Long postId);

    List<PostPhoto> findByPostId(Long postId);
}
