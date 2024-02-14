package idorm.idormServer.matchingMate.adapter.out.persistence;

import idorm.idormServer.matchingMate.domain.MatchingMate;
import idorm.idormServer.matchingMate.domain.MatchingMates;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingMatesMapper {

    private final MatchingMateMapper matchingMateMapper;

    public MatchingMatesEmbeddedEntity toEntity(MatchingMates matchingMates) {
        return new MatchingMatesEmbeddedEntity(getFavoriteMatesEntity(matchingMates),
                getNonFavoriteMatesEntity(matchingMates));
    }

    public MatchingMates toDomain(MatchingMatesEmbeddedEntity matchingMatesEntity) {
        return MatchingMates.forMapper(getFavoriteMates(matchingMatesEntity), getNonFavoriteMates(matchingMatesEntity));
    }

    private Set<MatchingMateJpaEntity> getFavoriteMatesEntity(MatchingMates matchingMates) {
        Set<MatchingMateJpaEntity> favoriteMatesEntity = matchingMates.getFavoriteMates().stream()
                .map(matchingMateMapper::toEntity)
                .collect(Collectors.toSet());

        return favoriteMatesEntity;
    }

    private Set<MatchingMateJpaEntity> getNonFavoriteMatesEntity(MatchingMates matchingMates) {
        Set<MatchingMateJpaEntity> nonFavoriteMatesEntity = matchingMates.getNonFavoriteMates().stream()
                .map(matchingMateMapper::toEntity)
                .collect(Collectors.toSet());

        return nonFavoriteMatesEntity;
    }

    private Set<MatchingMate> getFavoriteMates(MatchingMatesEmbeddedEntity mates) {
        Set<MatchingMate> favoriteMates = mates.getFavoriteMates().stream()
                .map(matchingMateMapper::toDomain)
                .collect(Collectors.toSet());

        return favoriteMates;
    }

    private Set<MatchingMate> getNonFavoriteMates(MatchingMatesEmbeddedEntity mates) {
        Set<MatchingMate> nonFavoriteMates = mates.getNonFavoriteMates().stream()
                .map(matchingMateMapper::toDomain)
                .collect(Collectors.toSet());

        return nonFavoriteMates;
    }
}
