package idorm.idormServer.matchingInfo.adapter.out.persistence;

import idorm.idormServer.matchingInfo.domain.TextInfo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TextInfoMapper {

    public TextInfoEmbeddedEntity toEntity(TextInfo textInfo) {
        return new TextInfoEmbeddedEntity(textInfo.getWakeUpTime(),
                textInfo.getCleanUpStatus(),
                textInfo.getShowerTime(),
                textInfo.getWishText(),
                textInfo.getMbti());
    }

    public TextInfo toDomain(TextInfoEmbeddedEntity entity) {
        return TextInfo.forMapper(entity.getWakeUpTime(),
                entity.getCleanUpStatus(),
                entity.getShowerTime(),
                entity.getWishText(),
                entity.getMbti());
    }
}