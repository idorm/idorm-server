package idorm.idormServer.email.application.port.out;

import idorm.idormServer.email.entity.Email;

public interface SendEmailPort {
    void send(Email email);
}