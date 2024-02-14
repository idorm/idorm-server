package idorm.idormServer.matchingInfo.adapter.out.persistence;

import idorm.idormServer.matchingInfo.domain.DormInfo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class DormInfoMapper {

    public DormInfoEmbeddedEntity toEntity(DormInfo dormInfo) {
        return new DormInfoEmbeddedEntity(dormInfo.getDormCategory(), dormInfo.getJoinPeriod(), dormInfo.getGender());
    }

    public DormInfo toDomain(DormInfoEmbeddedEntity dormInfoEmbeddedEntity) {
        return DormInfo.forMapper(dormInfoEmbeddedEntity.getDormCategory(), dormInfoEmbeddedEntity.getJoinPeriod(),
                dormInfoEmbeddedEntity.getGender());
    }
}