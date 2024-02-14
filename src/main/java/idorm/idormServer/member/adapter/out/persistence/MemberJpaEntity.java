package idorm.idormServer.member.adapter.out.persistence;

import static idorm.idormServer.email.domain.Email.MAX_EMAIL_LENGTH;

import idorm.idormServer.matchingMate.adapter.out.persistence.MatchingMatesEmbeddedEntity;
import idorm.idormServer.member.domain.MemberStatus;
import idorm.idormServer.member.domain.RoleType;
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
public class MemberJpaEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "ENUM('ACTIVE', 'DELETED')")
    private MemberStatus memberStatus;

    @Column(nullable = false, length = MAX_EMAIL_LENGTH)
    private String email;

    @Embedded
    private NicknameEmbeddedEntity nickname;

    @Embedded
    private PasswordEmbeddedEntity password;

    @Embedded
    private MemberPhotoEmbeddedEntity memberPhoto;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('USER', 'ADMIN')")
    private RoleType roleType;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Embedded
    private MatchingMatesEmbeddedEntity matchingMates;
}