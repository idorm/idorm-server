package idorm.idormServer.matchingInfo.adapter.out.persistence;

import idorm.idormServer.matchingInfo.domain.SharedURL;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SharedURLMapper {

    public SharedURLEmbeddedEntity toEntity(SharedURL sharedURL) {
        return new SharedURLEmbeddedEntity(sharedURL.getValue());
    }

    public SharedURL toDomain(SharedURLEmbeddedEntity entity) {
        return SharedURL.forMapper(entity.getValue());
    }
}