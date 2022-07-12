package idorm.idormServer.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue
    @Column(name="member_id")
    private Long id;

    private String email;
    private String password;

    @OneToOne
    @JoinColumn(name="myinfo_member_id")
    private MyInfo myInfo;

    public Member(String email, String password) {
        this.email = email;
        this.password = password;

    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


    /**
     * 비지니스 로직
     */
    public void updatePassword(String password) {
        this.password = password;
    }


}
