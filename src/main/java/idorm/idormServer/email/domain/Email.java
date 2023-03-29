package idorm.idormServer.email.domain;

import idorm.idormServer.common.BaseEntity;
import idorm.idormServer.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email extends BaseEntity {

    @Id
    @Column(name="email_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String email;

    private String code;

    private Boolean isCheck;

    private Boolean isPossibleUpdatePassword;

    private LocalDateTime isPossibleUpdatePasswordCreatedAt; // 비밀번호 변경 유효 시작 시

    @Builder
    public Email(String email, String code) {
        this.email = email;
        this.code = code;
        this.isCheck = false;
        this.isPossibleUpdatePassword = false;
        this.isPossibleUpdatePasswordCreatedAt = LocalDateTime.now();
        this.setIsDeleted(false);
    }

    public void isChecked() {
        this.isCheck = true;
    }

    public void isJoined(Member member) {
        this.member = member;
        if (!member.getEmails().contains(this)) {
            member.getEmails().add(this);
        }
    }

    public void updateCode(String code) {
        this.code = code;
    }

    public void updateIsPossibleUpdatePassword(Boolean isPossibleUpdatePassword) {
        this.isPossibleUpdatePassword = isPossibleUpdatePassword;
    }

    public void updateIsPossibleUpdatePasswordCreatedAt() {
        this.isPossibleUpdatePasswordCreatedAt = LocalDateTime.now();
    }

    public void deleteData() {
        this.email = null;
        this.code = null;
        this.isCheck = null;
        this.isPossibleUpdatePassword = null;
        this.isPossibleUpdatePasswordCreatedAt = null;
    }

    public void delete() {
        this.setIsDeleted(true);
    }
}
