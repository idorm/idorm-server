package idorm.idormServer.email.adapter.out.persistence;

import idorm.idormServer.email.domain.Email;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailMapper {

    private final VerificationMapper verificationMapper;

    public EmailJpaEntity toEntity(Email email) {
        return new EmailJpaEntity(email.getId(),
                email.getEmail(),
                verificationMapper.toEntity(email.getCode()),
                email.getIssuedAt(),
                email.isRegistered(),
                email.getEmailStatus());
    }

    public Email toDomain(EmailJpaEntity emailEntity) {
        return Email.forMapper(emailEntity.getId(),
                emailEntity.getEmail(),
                emailEntity.getEmailStatus(),
                verificationMapper.toDomain(emailEntity.getCode()),
                emailEntity.getIssuedAt(),
                emailEntity.isRegistered());
    }
}