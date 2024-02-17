package idorm.idormServer.email.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import idorm.idormServer.email.adapter.out.api.exception.NotFoundEmailException;
import idorm.idormServer.email.application.port.out.LoadEmailPort;
import idorm.idormServer.email.domain.Email;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LoadEmailAdapter implements LoadEmailPort {

	private final EmailRepository emailRepository;
	private final EmailMapper emailMapper;

	@Override
	public Optional<Email> findByEmailWithOptional(final String email) {
		final Optional<EmailJpaEntity> optionalEmail = emailRepository.findByEmail(email);
		return Optional.ofNullable(optionalEmail.map(emailMapper::toDomain).orElse(null));
	}

	@Override
	public Email findByEmail(final String email) {
		EmailJpaEntity emailJpaEntity = emailRepository.findByEmail(email)
			.orElseThrow(NotFoundEmailException::new);
		return emailMapper.toDomain(emailJpaEntity);
	}
}