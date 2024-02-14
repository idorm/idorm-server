package idorm.idormServer.member.domain;

import static idorm.idormServer.common.exception.ExceptionCode.CANNOT_UPDATE_NICKNAME;

import idorm.idormServer.common.exception.CustomException;
import idorm.idormServer.common.exception.ExceptionCode;
import idorm.idormServer.common.util.Validator;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = "value")
public class Nickname {

    private static final String NICKNAME_REGEX = "^[A-Za-z0-9ㄱ-ㅎ가-힣]{2,8}+$";
    private static final int VALID_UPDATE_DAY = 30;

    private String value;
    private LocalDateTime createdAt;

    public Nickname(final String value) {
        validate(value);
        this.value = value;
        this.createdAt = LocalDateTime.now();
    }

    private void validate(final String value) {
        Validator.validateNotBlank(value);
        Validator.validateFormat(value, NICKNAME_REGEX, ExceptionCode.NICKNAME_CHARACTER_INVALID);

    }

    public static Nickname forMapper(final String nickname) {
        return new Nickname(nickname);
    }

    public void validateValidUpdate(final LocalDateTime now) {
        if (now.isBefore(this.createdAt.plusDays(VALID_UPDATE_DAY))) {
            throw new CustomException(null, CANNOT_UPDATE_NICKNAME);
        }
    }
}