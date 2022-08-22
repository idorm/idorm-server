package idorm.idormServer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email {

    @Id @GeneratedValue
    @Column(name="email_id")
    private Long id;

    private String email;
    private String code;
    private boolean isCheck; // 인증 여부
    private boolean isJoin; // 가입 여부

    public Email(String email, String code) {
        this.email = email;
        this.code = code;
        this.isCheck = false;
    }

    public void isChecked() {
        this.isCheck = true;
    } // 인증 여부
    public void isJoined() {
        this.isJoin = true;
    } // 가입 여부

    public boolean getJoined() {
        return this.isJoin;
    }
    public void setCode(String code) {
        this.code = code;
    }
}
