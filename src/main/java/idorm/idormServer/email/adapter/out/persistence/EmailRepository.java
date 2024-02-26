package idorm.idormServer.email.adapter.out.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import idorm.idormServer.email.entity.Email;

public interface EmailRepository extends JpaRepository<Email, Long> {

	Optional<Email> findByEmail(String email);
}