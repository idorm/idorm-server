package idorm.idormServer.matching.domain;

import idorm.idormServer.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DislikedMember {

    @Id
    @Column(name = "disliked_member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private Long selectedDislikedMemberId;

    @Builder
    public DislikedMember(Member loginMember, Long dislikedMemberId) {
        this.member = loginMember;
        this.selectedDislikedMemberId = dislikedMemberId;
    }
}
