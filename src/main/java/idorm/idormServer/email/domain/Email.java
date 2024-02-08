package idorm.idormServer.email.domain;

import static idorm.idormServer.exception.ExceptionCode.EMAIL_CHARACTER_INVALID;
import static idorm.idormServer.exception.ExceptionCode.FIELD_REQUIRED;

import idorm.idormServer.exception.CustomException;
import idorm.idormServer.exception.ExceptionCode;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Email {

    private static final Pattern EMAIL_REGEX = Pattern.compile("^([\\w-]+(?:\\.[\\w-]+)*)+@(inu.ac.kr)$");
    private static final long VALID_VERIFY_MINUTE = 5L;
    private static final long VALID_REGISTER_MINUTE = 10L;

    @Id
    @Column(name = "email_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Embedded
    @Column(nullable = false)
    private VerificationCode code;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime issuedAt;

    @Column(nullable = false)
    private boolean registered;

    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "ENUM('SEND', 'VERIFIED', 'RE_SEND', 'RE_VERIFIED')")
    private EmailStatus emailStatus;

    @Builder
    public Email(String email, VerificationCode code, LocalDateTime issuedAt) {
        validate(email);
        this.email = email;
        this.code = code;
        this.emailStatus = EmailStatus.SEND;
        this.registered = false;
        this.issuedAt = issuedAt;
    }

    public void updateCode(VerificationCode code, LocalDateTime issuedAt) {
        this.code = code;
        this.issuedAt = issuedAt;

        if (this.registered) {
            this.emailStatus = EmailStatus.RE_SEND;
            return;
        }
        this.emailStatus = EmailStatus.SEND;
    }

    public void verify(String code, LocalDateTime now) {
        verifyTime(now);
        this.code.verify(code);

        if (this.registered) {
            this.emailStatus = EmailStatus.RE_VERIFIED;
            return;
        }
        this.emailStatus = EmailStatus.VERIFIED;
    }

    public void register() {
        this.registered = true;
    }

    private void verifyTime(LocalDateTime now) {
        LocalDateTime expiredTime = this.issuedAt.plusMinutes(VALID_VERIFY_MINUTE);
        if (now.isAfter(expiredTime)) {
            throw new CustomException(null, ExceptionCode.EXPIRED_CODE);
        }
    }

    public void verifyRegisterTime(LocalDateTime now) {
        LocalDateTime expiredTime = this.issuedAt.plusMinutes(VALID_REGISTER_MINUTE);
        if (now.isAfter(expiredTime)) {
            throw new CustomException(null, ExceptionCode.EXPIRED_CODE);
        }
    }

    private void validate(final String value) {
        if (Objects.isNull(value)) {
            throw new CustomException(null, FIELD_REQUIRED);
        }

        if (isNotMatchEmailFormat(value)) {
            throw new CustomException(null, EMAIL_CHARACTER_INVALID);
        }
    }
    
    private boolean isNotMatchEmailFormat(final String value) {
        return !EMAIL_REGEX.matcher(value).matches();
    }
}
