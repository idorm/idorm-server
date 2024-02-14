package idorm.idormServer.matchingInfo.adapter.out.persistence;

import idorm.idormServer.matchingInfo.domain.Age;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AgeMapper {

    public AgeEmbeddedEntity toEntity(Age age) {
        return new AgeEmbeddedEntity(age.getValue());
    }

    public Age toDomain(AgeEmbeddedEntity ageEmbeddedEntity) {
        return Age.forMapper(ageEmbeddedEntity.getValue());
    }
}