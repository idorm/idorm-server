package idorm.idormServer.matchingInfo.adapter.out.persistence;

import idorm.idormServer.matchingInfo.domain.PreferenceInfo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PreferenceInfoMapper {

    private final AgeMapper ageMapper;

    public PreferenceInfoEmbeddedEntity toEntity(PreferenceInfo preferenceInfo) {
        return new PreferenceInfoEmbeddedEntity(preferenceInfo.getIsSnoring(),
                preferenceInfo.getIsGrinding(),
                preferenceInfo.getIsSmoking(),
                preferenceInfo.getIsAllowedFood(),
                preferenceInfo.getIsWearEarphones(),
                ageMapper.toEntity(preferenceInfo.getAge()));
    }

    public PreferenceInfo toDomain(PreferenceInfoEmbeddedEntity entity) {
        return PreferenceInfo.forMapper(entity.getIsSnoring(),
                entity.getIsGrinding(),
                entity.getIsSmoking(),
                entity.getIsAllowedFood(),
                entity.getIsWearEarphones(),
                ageMapper.toDomain(entity.getAge()));
    }
}