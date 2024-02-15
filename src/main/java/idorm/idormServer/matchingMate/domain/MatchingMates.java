package idorm.idormServer.matchingMate.domain;

import idorm.idormServer.common.exception.CustomException;
import idorm.idormServer.common.exception.ExceptionCode;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MatchingMates {

    private Set<MatchingMate> favoriteMates = new HashSet<>();
    private Set<MatchingMate> nonFavoriteMates = new HashSet<>();

    public static MatchingMates empty() {
        return new MatchingMates();
    }

    public static MatchingMates forMapper(Set<MatchingMate> favoriteMates, Set<MatchingMate> nonFavoriteMates) {
        return new MatchingMates(favoriteMates, nonFavoriteMates);
    }

    void addFavoriteMate(MatchingMate mate) {
        validateUniqueFavoriteMate(mate);
        favoriteMates.add(mate);

        if (isNonFavoriteMate(mate)) {
            deleteNonFavoriteMate(mate);
        }
    }

    void deleteNonFavoriteMate(MatchingMate mate) {
        Set<MatchingMate> deletedMates = nonFavoriteMates.stream()
                .filter(nonFavoriteMate -> !nonFavoriteMate.equals(mate))
                .collect(Collectors.toSet());

        nonFavoriteMates.clear();
        nonFavoriteMates.addAll(deletedMates);
    }

    void addNonFavoriteMate(MatchingMate mate) {
        validateUniqueNonFavoriteMate(mate);
        nonFavoriteMates.add(mate);

        if (isFavoriteMate(mate)) {
            deleteFavoriteMate(mate);
        }
    }

    void deleteFavoriteMate(MatchingMate mate) {
        Set<MatchingMate> deletedMates = favoriteMates.stream()
                .filter(favoriteMate -> !favoriteMate.equals(mate))
                .collect(Collectors.toSet());

        favoriteMates.clear();
        favoriteMates.addAll(deletedMates);
    }

    private boolean isFavoriteMate(MatchingMate mate) {
        return favoriteMates.contains(mate);
    }

    private boolean isNonFavoriteMate(MatchingMate mate) {
        return nonFavoriteMates.contains(mate);
    }

    private void validateUniqueFavoriteMate(MatchingMate mate) {
        if (favoriteMates.contains(mate)) {
            throw new CustomException(null, ExceptionCode.DUPLICATE_LIKED_MEMBER);
        }
    }

    private void validateUniqueNonFavoriteMate(MatchingMate mate) {
        if (nonFavoriteMates.contains(mate)) {
            throw new CustomException(null, ExceptionCode.DUPLICATE_DISLIKED_MEMBER);
        }
    }
}