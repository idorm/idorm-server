package idorm.idormServer.photo.repository;

import idorm.idormServer.photo.domain.OfficialCalendarPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfficialCalendarPhotoRepository extends JpaRepository<OfficialCalendarPhoto, Long> {
}
