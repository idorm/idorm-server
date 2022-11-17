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
public class LikedMember extends BaseEntity {

    @Id
    @Column(name = "liked_member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long selectedLikedMemberId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;


    @Builder
    public LikedMember(Member loginMember, Long likedMemberId) {
        this.member = loginMember;
        this.selectedLikedMemberId = likedMemberId;
    }
}
