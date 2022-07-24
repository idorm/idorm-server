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

//    @JsonIgnore // JPA 순환참조 해결
//    @OneToOne(mappedBy = "email")
//    private Member member;

    public Email(String email, String code) {
        this.email = email;
        this.code = code;
        this.isCheck = false;
    }

    public void isChecked() {
        this.isCheck = true;
    }
    public void isJoined() {
        this.isJoin = true;
    }

    public boolean getJoined() {
        return this.isJoin;
    }
    public void setCode(String code) {
        this.code = code;
    }
}
