package idorm.idormServer.email.application.port.out;

import idorm.idormServer.email.domain.Email;

public interface SaveEmailPort {
    void save(Email email);
}