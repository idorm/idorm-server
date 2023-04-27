package idorm.idormServer.photo.repository;

import idorm.idormServer.photo.domain.MemberPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberPhotoRepository extends JpaRepository<MemberPhoto, Long> {
}
