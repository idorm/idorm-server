package idorm.idormServer.community.postPhoto.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import idorm.idormServer.community.post.adapter.out.persistence.PostJpaEntity;

public interface PostPhotoRepository extends JpaRepository<PostPhotoJpaEntity, Long> {

	Optional<PostPhotoJpaEntity> findByIdAndPostId(Long id, Long postId);

	List<PostPhotoJpaEntity> findAllByPost(PostJpaEntity postJpaEntity);

	List<PostPhotoJpaEntity> findAllByPostId(Long postId);

	@Modifying
	void deleteAllByPostId(Long postId);
}
