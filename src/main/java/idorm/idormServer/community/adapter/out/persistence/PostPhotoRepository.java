package idorm.idormServer.community.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostPhotoRepository extends JpaRepository<PostPhotoJpaEntity, Long> {

	Optional<PostPhotoJpaEntity> findByIdAndPostIdAndIsDeletedIsFalse(Long id, Long postId);

	List<PostPhotoJpaEntity> findAllByPostAndIsDeletedIsFalse(PostJpaEntity postJpaEntity);
}
