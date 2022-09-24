package idorm.idormServer.email.repository;

import idorm.idormServer.email.domain.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmailRepository extends JpaRepository<Email,Long> {

    Optional<Email> findByEmail(String email);
}