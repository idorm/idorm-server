package idorm.idormServer.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Matching {

    @Id
    @GeneratedValue
    @Column(name="matching_id")
    private Long id;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private List<Member> matchingMem; // 매칭되는 상대 멤버들

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member; // 로그인한 멤버
}
