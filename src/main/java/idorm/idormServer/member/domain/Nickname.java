package idorm.idormServer.member.domain;

import static idorm.idormServer.common.exception.ExceptionCode.CANNOT_UPDATE_NICKNAME;

import idorm.idormServer.common.exception.CustomException;
import idorm.idormServer.common.exception.ExceptionCode;
import idorm.idormServer.common.util.Validator;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Nickname {

    private static final String NICKNAME_REGEX = "^[A-Za-z0-9ㄱ-ㅎ가-힣]{2,8}+$";
    private static final int VALID_UPDATE_DAY = 30;

    @Column(name = "nickname", nullable = false, unique = true)
    private String value;

    @CreatedDate
    @Column(name = "nickname_created_at", updatable = false)
    private LocalDateTime createdAt;

    public Nickname(final Member member, final String value) {
        validate(value);
        this.value = value;
        this.createdAt = LocalDateTime.now();

        member.updateNickname(this);
    }

    private void validate(final String value) {
        Validator.validateNotBlank(value);
        Validator.validateFormat(value, NICKNAME_REGEX, ExceptionCode.NICKNAME_CHARACTER_INVALID);

    }

    public void validateValidUpdate(final LocalDateTime now) {
        if (now.isBefore(this.createdAt.plusDays(VALID_UPDATE_DAY))) {
            throw new CustomException(null, CANNOT_UPDATE_NICKNAME);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Nickname nickname = (Nickname) o;
        return Objects.equals(value, nickname.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
