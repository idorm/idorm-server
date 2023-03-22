package idorm.idormServer.email.domain;

import idorm.idormServer.common.BaseEntity;
import idorm.idormServer.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @Builder
    public Email(String email, String code) {
        this.email = email;
        this.code = code;
        this.isCheck = false;
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

    public void delete() {
        this.setIsDeleted(true);
    }
}
