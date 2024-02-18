package idorm.idormServer.member.adapter.out.persistence;

import idorm.idormServer.member.domain.Password;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PasswordMapper {

    PasswordEmbeddedEntity toEntity(Password password) {
        return new PasswordEmbeddedEntity(password.getValue());
    }

    Password toDomain(PasswordEmbeddedEntity passwordEntity) {
        return Password.forMapper(passwordEntity.getValue());
    }
}