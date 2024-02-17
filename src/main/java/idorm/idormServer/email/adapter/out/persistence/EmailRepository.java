package idorm.idormServer.email.adapter.out.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<EmailJpaEntity, Long> {

	Optional<EmailJpaEntity> findByEmail(String email);
}