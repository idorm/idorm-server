package idorm.idormServer.member.repository;

import idorm.idormServer.member.domain.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailRepository extends JpaRepository<Email,Long> {

    Optional<Email> findByEmailAndIsCheckIsTrueAndIsDeletedIsFalseAndMemberIsNotNull(String email);

    Optional<Email> findByEmailAndIsDeletedIsFalse(String email);

    Email findByMemberIdAndIsDeletedIsFalse(Long memberId);
}