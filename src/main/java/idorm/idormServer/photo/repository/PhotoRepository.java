package idorm.idormServer.photo.repository;

import idorm.idormServer.photo.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    Optional<Photo> findByIdAndPostIdAndIsDeletedFalse(Long id, Long postId);

    Optional<Photo> findByMemberId(Long memberId);

    List<Photo> findByFolderName(String folderName);

    List<Photo> findByPostId(Long postId);

    boolean existsByMemberId(Long memberId);
}
