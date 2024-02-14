package idorm.idormServer.member.domain;

import static idorm.idormServer.common.exception.ExceptionCode.PASSWORD_CHARACTER_INVALID;

import idorm.idormServer.auth.encryptor.EncryptorI;
import idorm.idormServer.common.util.Validator;
import lombok.Getter;

@Getter
public class Password {

    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,15}$";

    private String value;

    private Password(final String value) {
        this.value = value;
    }

    public static Password from(final String password) {
        return new Password(password);
    }

    public static Password of(EncryptorI encryptor, Member member, String password) {
        validate(password);
        Password createdPassword = new Password(encryptor.encrypt(password));

        member.updatePassword(createdPassword);
        return createdPassword;
    }

    public static Password forMapper(final String password) {
        return new Password(password);
    }

    private static void validate(final String value) {
        Validator.validateNotBlank(value);
        Validator.validateFormat(value, PASSWORD_REGEX, PASSWORD_CHARACTER_INVALID);
    }
}