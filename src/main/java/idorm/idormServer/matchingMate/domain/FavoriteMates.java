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
public class FavoriteMates {

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Mate> favoriteMates = new HashSet<>();

    public static FavoriteMates empty() {
        return new FavoriteMates();
    }

    public void addFavoriteMate(Mate mate) {
        favoriteMates.add(mate);
    }

    public void deleteFavoriteMate(Member targetMember) {
        Set<Mate> deletedMates = favoriteMates.stream()
                .filter(mate -> !mate.isSameTarget(targetMember))
                .collect(Collectors.toSet());

        favoriteMates.clear();
        favoriteMates.addAll(deletedMates);
    }

    public boolean isDuplicated(Member favoriteMember) {
        return favoriteMates.stream()
                .anyMatch(mate -> mate.isSameTarget(favoriteMember));
    }
}
