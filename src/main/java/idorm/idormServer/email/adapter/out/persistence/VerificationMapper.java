package idorm.idormServer.email.adapter.out.persistence;

import idorm.idormServer.email.domain.VerificationCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class VerificationMapper {

    public VerificationCodeEmbeddedEntity toEntity(VerificationCode code) {
        return new VerificationCodeEmbeddedEntity(code.getValue());
    }

    public VerificationCode toDomain(VerificationCodeEmbeddedEntity codeEntity) {
        return VerificationCode.forMapper(codeEntity.getValue());
    }
}