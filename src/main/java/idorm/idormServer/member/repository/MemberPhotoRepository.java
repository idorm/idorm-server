package idorm.idormServer.member.repository;

import idorm.idormServer.member.domain.MemberPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberPhotoRepository extends JpaRepository<MemberPhoto, Long> {

    MemberPhoto findByMemberIdAndIsDeletedIsFalse(Long memberId);
}
