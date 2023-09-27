package idorm.idormServer.photo.repository;

import idorm.idormServer.photo.domain.MemberPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberPhotoRepository extends JpaRepository<MemberPhoto, Long> {

    MemberPhoto findByMemberIdAndIsDeletedIsFalse(Long memberId);
}
