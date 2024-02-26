package idorm.idormServer.email.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.email.application.port.out.SaveEmailPort;
import idorm.idormServer.email.entity.Email;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SaveEmailAdapter implements SaveEmailPort {

	private final EmailRepository emailRepository;

	@Override
	public void save(final Email email) {
		emailRepository.save(email);
	}
}
