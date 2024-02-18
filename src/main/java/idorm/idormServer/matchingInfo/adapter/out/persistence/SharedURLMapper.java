package idorm.idormServer.matchingInfo.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.matchingInfo.domain.SharedURL;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SharedURLMapper {

	SharedURLEmbeddedEntity toEntity(SharedURL sharedURL) {
		return new SharedURLEmbeddedEntity(sharedURL.getValue());
	}

	SharedURL toDomain(SharedURLEmbeddedEntity entity) {
		return SharedURL.forMapper(entity.getValue());
	}
}