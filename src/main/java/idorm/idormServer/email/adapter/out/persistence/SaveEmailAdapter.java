package idorm.idormServer.email.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.email.application.port.out.SaveEmailPort;
import idorm.idormServer.email.domain.Email;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SaveEmailAdapter implements SaveEmailPort {

	private final EmailMapper emailMapper;
	private final EmailRepository emailRepository;

	@Override
	public void save(final Email email) {
		final EmailJpaEntity emailEntity = emailMapper.toEntity(email);
		emailRepository.save(emailEntity);
		email.assignId(emailEntity.getId());
	}
}
