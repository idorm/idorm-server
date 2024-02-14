package idorm.idormServer.email.adapter.out.persistence;

import static idorm.idormServer.email.domain.Email.MAX_EMAIL_LENGTH;

import idorm.idormServer.email.domain.EmailStatus;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailJpaEntity {

    @Id
    @Column(name = "email_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = MAX_EMAIL_LENGTH)
    private String email;

    @Embedded
    private VerificationCodeEmbeddedEntity code;

    @Column(nullable = false)
    private LocalDateTime issuedAt;

    @Column(nullable = false)
    private boolean registered;

    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "ENUM('SEND', 'VERIFIED', 'RE_SEND', 'RE_VERIFIED')")
    private EmailStatus emailStatus;
}