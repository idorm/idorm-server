package idorm.idormServer.member.adapter.out.persistence;

import idorm.idormServer.member.domain.Password;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PasswordMapper {

    public PasswordEmbeddedEntity toEntity(Password password) {
        return new PasswordEmbeddedEntity(password.getValue());
    }

    public Password toDomain(PasswordEmbeddedEntity passwordEntity) {
        return Password.forMapper(passwordEntity.getValue());
    }
}