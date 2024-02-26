package idorm.idormServer.email.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import idorm.idormServer.email.adapter.out.api.exception.NotFoundEmailException;
import idorm.idormServer.email.application.port.out.LoadEmailPort;
import idorm.idormServer.email.entity.Email;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LoadEmailAdapter implements LoadEmailPort {

	private final EmailRepository emailRepository;

	@Override
	public Optional<Email> findByEmailWithOptional(final String email) {
		return emailRepository.findByEmail(email);
	}

	@Override
	public Email findByEmail(final String email) {
		return emailRepository.findByEmail(email)
			.orElseThrow(NotFoundEmailException::new);
	}
}