package idorm.idormServer.matchingMate.adapter.out.persistence;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingMatesEmbeddedEntity {

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<MatchingMateJpaEntity> favoriteMates = new HashSet<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<MatchingMateJpaEntity> nonFavoriteMates = new HashSet<>();

    MatchingMatesEmbeddedEntity(Set<MatchingMateJpaEntity> favoriteMates, Set<MatchingMateJpaEntity> nonFavoriteMates) {
        this.favoriteMates = favoriteMates;
        this.nonFavoriteMates = nonFavoriteMates;
    }
}