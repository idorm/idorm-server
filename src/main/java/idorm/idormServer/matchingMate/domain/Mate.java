package idorm.idormServer.matchingMate.domain;

import idorm.idormServer.member.domain.Member;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Mate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mate_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "target_member_id")
    private Member targetMember;

    @Builder
    public Mate(Member loginMember, Member targetMember, boolean isFavorite) {
        this.member = loginMember;
        this.targetMember = targetMember;

        if (isFavorite) {
            loginMember.validateUniqueFavoriteMate(targetMember);
            loginMember.getFavoriteMates().addFavoriteMate(this);
            return;
        }
        loginMember.validateUniqueNonFavoriteMate(targetMember);
        loginMember.getNonFavoriteMates().addNonFavoriteMate(this);
    }

    public boolean isSameTarget(Member targetMember) {
        return this.targetMember.equals(targetMember);
    }
}
