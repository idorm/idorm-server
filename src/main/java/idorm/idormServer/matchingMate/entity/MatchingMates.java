package idorm.idormServer.matchingMate.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import idorm.idormServer.matchingMate.adapter.out.exception.DuplicatedFavoriteMateException;
import idorm.idormServer.matchingMate.adapter.out.exception.DuplicatedNonFavoriteMateException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingMates {

	// cascade(영속성 전이)-PERSIST(저장) : 부모(MatchingMates) 영속화 시, 자식(MatchingMate)도 영속화 시킨다.
	// cascade(영속성 전이)-REMOVE(삭제) : 부모(MatchingMates) 삭제 시, 자식(MatchingMate)도 삭제 시킨다.
	// 외래키 조건에 따라 삭제 시, 자식 먼저 삭제 후 부모를 삭제 시킨다.

	// TODO: fav, nonFav를 하나로 합친다? 중복 체크 검증 체크
	@OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST,
		CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<MatchingMate> favoriteMates = new HashSet<>();

	@OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST,
		CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<MatchingMate> nonFavoriteMates = new HashSet<>();

	public static MatchingMates empty() {
		return new MatchingMates();
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