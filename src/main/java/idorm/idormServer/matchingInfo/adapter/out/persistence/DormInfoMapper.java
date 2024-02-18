package idorm.idormServer.matchingInfo.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.matchingInfo.domain.DormInfo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class DormInfoMapper {

	DormInfoEmbeddedEntity toEntity(DormInfo dormInfo) {
		return new DormInfoEmbeddedEntity(dormInfo.getDormCategory(), dormInfo.getJoinPeriod(), dormInfo.getGender());
	}

	DormInfo toDomain(DormInfoEmbeddedEntity dormInfoEmbeddedEntity) {
		return DormInfo.forMapper(dormInfoEmbeddedEntity.getDormCategory(), dormInfoEmbeddedEntity.getJoinPeriod(),
			dormInfoEmbeddedEntity.getGender());
	}
}