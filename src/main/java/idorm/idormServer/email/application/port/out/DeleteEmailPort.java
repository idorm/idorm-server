package idorm.idormServer.email.application.port.out;

import idorm.idormServer.email.domain.Email;

public interface DeleteEmailPort {
	void delete(Email email);
}
