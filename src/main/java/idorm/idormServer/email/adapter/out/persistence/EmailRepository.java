package idorm.idormServer.email.adapter.out.persistence;

import idorm.idormServer.email.domain.EmailStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailRepository extends JpaRepository<EmailJpaEntity,Long> {

    // TODO: 조회 시 status 고려
    Optional<EmailJpaEntity> findByEmail(String email);

    Optional<EmailJpaEntity> findByEmailAndEmailStatus(String email, EmailStatus emailStatus);
}