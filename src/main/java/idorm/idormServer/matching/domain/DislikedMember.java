package idorm.idormServer.matching.domain;

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
public class DislikedMember extends BaseEntity {

    @Id
    @Column(name = "disliked_member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long selectedDislikedMemberId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public DislikedMember(Member loginMember, Long dislikedMemberId) {
        this.member = loginMember;
        this.selectedDislikedMemberId = dislikedMemberId;
    }
}
