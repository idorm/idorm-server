package idorm.idormServer.matchingMate.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import idorm.idormServer.matchingMate.adapter.out.exception.DuplicatedFavoriteMateException;
import idorm.idormServer.matchingMate.adapter.out.exception.DuplicatedNonFavoriteMateException;
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

	public static MatchingMates forMapper(final Set<MatchingMate> favoriteMates,
		final Set<MatchingMate> nonFavoriteMates) {

		return new MatchingMates(favoriteMates, nonFavoriteMates);
	}

	public void addFavoriteMate(final MatchingMate mate) {
		validateUniqueFavoriteMate(mate);
		favoriteMates.add(mate);

		if (isNonFavoriteMate(mate)) {
			deleteNonFavoriteMate(mate);
		}
	}

	public void deleteNonFavoriteMate(final MatchingMate mate) {
		Set<MatchingMate> deletedMates = nonFavoriteMates.stream()
			.filter(nonFavoriteMate -> !nonFavoriteMate.equals(mate))
			.collect(Collectors.toSet());

		nonFavoriteMates.clear();
		nonFavoriteMates.addAll(deletedMates);
	}

	public void addNonFavoriteMate(final MatchingMate mate) {
		validateUniqueNonFavoriteMate(mate);
		nonFavoriteMates.add(mate);

		if (isFavoriteMate(mate)) {
			deleteFavoriteMate(mate);
		}
	}

	public void deleteFavoriteMate(final MatchingMate mate) {
		Set<MatchingMate> deletedMates = favoriteMates.stream()
			.filter(favoriteMate -> !favoriteMate.equals(mate))
			.collect(Collectors.toSet());

		favoriteMates.clear();
		favoriteMates.addAll(deletedMates);
	}

	private boolean isFavoriteMate(final MatchingMate mate) {
		return favoriteMates.contains(mate);
	}

	public boolean isNonFavoriteMate(final MatchingMate mate) {
		return nonFavoriteMates.contains(mate);
	}

	private void validateUniqueFavoriteMate(final MatchingMate mate) {
		if (favoriteMates.contains(mate)) {
			throw new DuplicatedFavoriteMateException();
		}
	}

	private void validateUniqueNonFavoriteMate(final MatchingMate mate) {
		if (nonFavoriteMates.contains(mate)) {
			throw new DuplicatedNonFavoriteMateException();
		}
	}
}