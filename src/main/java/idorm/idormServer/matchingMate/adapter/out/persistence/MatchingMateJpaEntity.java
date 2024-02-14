package idorm.idormServer.matchingMate.adapter.out.persistence;

import idorm.idormServer.member.adapter.out.persistence.MemberJpaEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class MatchingMateJpaEntity {

    @Id
    @Column(name = "mate_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberJpaEntity member;

    @ManyToOne
    @JoinColumn(name = "target_member_id")
    private MemberJpaEntity targetMember;

    MatchingMateJpaEntity(MemberJpaEntity loginMember, MemberJpaEntity targetMember) {
        this.member = loginMember;
        this.targetMember = targetMember;
    }
}