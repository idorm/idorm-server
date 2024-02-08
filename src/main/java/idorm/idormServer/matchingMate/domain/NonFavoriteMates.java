package idorm.idormServer.matchingMate.domain;

import idorm.idormServer.member.domain.Member;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Embeddable
public class NonFavoriteMates {

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Mate> nonFavoriteMates = new HashSet<>();

    public static NonFavoriteMates empty() {
        return new NonFavoriteMates();
    }

    public boolean isDuplicated(Member nonFavoriteMember) {
        return nonFavoriteMates.stream()
                .anyMatch(mate -> mate.isSameTarget(nonFavoriteMember));
    }

    public void addNonFavoriteMate(Mate mate) {
        nonFavoriteMates.add(mate);
    }

    public void deleteNonFavoriteMate(Member targetMember) {
        Set<Mate> deletedMates = nonFavoriteMates.stream()
                .filter(mate -> !mate.isSameTarget(targetMember))
                .collect(Collectors.toSet());

        nonFavoriteMates.clear();
        nonFavoriteMates.addAll(deletedMates);
    }
}
