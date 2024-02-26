package idorm.idormServer.email.application.port.out;

import idorm.idormServer.email.entity.Email;

public interface DeleteEmailPort {
	void delete(Email email);
}
