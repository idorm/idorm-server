package idorm.idormServer.photo.repository;

import idorm.idormServer.photo.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    Optional<Photo> findByFileName(String fileName);

    List<Photo> findByFolderName(String folderName);
}
