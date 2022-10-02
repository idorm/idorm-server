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
public class LikedMember {

    @Id
    @Column(name = "liked_member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private Long selectedLikedMemberId;

    @Builder
    public LikedMember(Member loginMember, Long likedMemberId) {
        this.member = loginMember;
        this.selectedLikedMemberId = likedMemberId;
    }

}
