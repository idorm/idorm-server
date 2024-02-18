package idorm.idormServer.matchingInfo.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.matchingInfo.domain.Age;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AgeMapper {

	AgeEmbeddedEntity toEntity(Age age) {
		return new AgeEmbeddedEntity(age.getValue());
	}

	Age toDomain(AgeEmbeddedEntity ageEmbeddedEntity) {
		return Age.forMapper(ageEmbeddedEntity.getValue());
	}
}