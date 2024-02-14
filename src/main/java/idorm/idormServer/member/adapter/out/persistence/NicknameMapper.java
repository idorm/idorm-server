package idorm.idormServer.member.adapter.out.persistence;

import idorm.idormServer.member.domain.Nickname;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class NicknameMapper {

    public NicknameEmbeddedEntity toEntity(Nickname nickname) {
        return new NicknameEmbeddedEntity(nickname.getValue());
    }

    public Nickname toDomain(NicknameEmbeddedEntity nicknameEntity) {
        return Nickname.forMapper(nicknameEntity.getValue());
    }
}