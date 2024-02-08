package idorm.idormServer.matchingMate.domain;

import idorm.idormServer.member.domain.Member;
import java.util.Objects;
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
            loginMember.getFavoriteMates().addMate(this);
        } else {
            loginMember.getNonFavoriteMates().addMate(this);
        }
    }

    boolean isSameTarget(Member targetMember) {
        return this.targetMember.equals(targetMember);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Mate mate = (Mate) object;
        return Objects.equals(member, mate.member) && Objects.equals(targetMember, mate.targetMember);
    }

    @Override
    public int hashCode() {
        return Objects.hash(member, targetMember);
    }
}