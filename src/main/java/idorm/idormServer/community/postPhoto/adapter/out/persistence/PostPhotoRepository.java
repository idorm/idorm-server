package idorm.idormServer.community.postPhoto.adapter.out.persistence;

import idorm.idormServer.community.postPhoto.entity.PostPhoto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostPhotoRepository extends JpaRepository<PostPhoto, Long> {

	List<PostPhoto> findAllByPostId(Long postId);
}