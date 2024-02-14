package idorm.idormServer.email.adapter.out.persistence;

import idorm.idormServer.email.application.port.out.LoadEmailPort;
import idorm.idormServer.email.domain.Email;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LoadEmailAdapter implements LoadEmailPort {

    private final EmailRepository emailRepository;
    private final EmailMapper emailMapper;

    @Override
    @Transactional(readOnly = true)
    public Email findByEmail(String email) {
        final Optional<EmailJpaEntity> optionalEmail = emailRepository.findByEmail(email);
        return optionalEmail.map(emailMapper::toDomain).orElse(null);
    }
}