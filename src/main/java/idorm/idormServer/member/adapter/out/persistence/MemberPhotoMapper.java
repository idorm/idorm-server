package idorm.idormServer.member.adapter.out.persistence;

import idorm.idormServer.member.domain.MemberPhoto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberPhotoMapper {

    public MemberPhotoEmbeddedEntity toEntity(MemberPhoto memberPhoto) {
        return new MemberPhotoEmbeddedEntity(memberPhoto.getValue());
    }

    public MemberPhoto toDomain(MemberPhotoEmbeddedEntity memberPhotoEntity) {
        return MemberPhoto.forMapper(memberPhotoEntity.getValue());
    }
}
