package idorm.idormServer.email.domain;

import idorm.idormServer.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email extends BaseEntity {

    @Id
    @Column(name="email_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String code;

    private boolean isCheck; // 인증 여부

    private boolean isJoin; // 가입 여부

    public Email(String email, String code) {
        this.email = email;
        this.code = code;
        this.isCheck = false;
        this.isJoin = false;
    }

    /**
     * 인증 여부
     */
    public void isChecked() {
        this.isCheck = true;
    }

    /**
     * 가입 여부
     */
    public void isJoined() {
        this.isJoin = true;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
