package idorm.idormServer.email.application.port.out;

import java.util.Optional;

import idorm.idormServer.email.entity.Email;

public interface LoadEmailPort {
    Optional<Email> findByEmailWithOptional(String email);

    Email findByEmail(String email);
}
