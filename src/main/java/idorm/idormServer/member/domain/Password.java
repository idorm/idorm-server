package idorm.idormServer.member.domain;

import static idorm.idormServer.common.exception.ExceptionCode.PASSWORD_CHARACTER_INVALID;

import idorm.idormServer.auth.encryptor.EncryptorI;
import idorm.idormServer.common.util.Validator;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Password {

    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,15}$";

    @Column(name = "password", nullable = false)
    private String value;

    private Password(final String value) {
        this.value = value;
    }

    public static Password of(EncryptorI encryptor, Member member, String password) {
        validate(password);
        Password createdPassword = new Password(encryptor.encrypt(password));

        member.updatePassword(createdPassword);
        return createdPassword;
    }

    private static void validate(final String value) {
        Validator.validateNotBlank(value);
        Validator.validateFormat(value, PASSWORD_REGEX, PASSWORD_CHARACTER_INVALID);
    }
}