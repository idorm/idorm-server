package idorm.idormServer.community.postPhoto.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.community.postPhoto.entity.PostPhoto;

@Repository
public interface PostPhotoRepository extends JpaRepository<PostPhoto, Long> {

	Optional<PostPhoto> findByIdAndPostId(Long id, Long postId);

	List<PostPhoto> findAllByPost(Post post);

	List<PostPhoto> findAllByPostId(Long postId);

	@Modifying
	void deleteAllByPostId(Long postId);
}
