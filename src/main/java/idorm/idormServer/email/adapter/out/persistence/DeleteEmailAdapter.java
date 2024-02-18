package idorm.idormServer.email.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.email.application.port.out.DeleteEmailPort;
import idorm.idormServer.email.domain.Email;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeleteEmailAdapter implements DeleteEmailPort {

	private final EmailRepository emailRepository;
	private final EmailMapper emailMapper;

	@Override
	public void delete(final Email email) {
		emailRepository.delete(emailMapper.toEntity(email));
	}
}