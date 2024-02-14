package idorm.idormServer.fcm.adapter.out.persistence;

import idorm.idormServer.fcm.domain.MemberFCM;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberFCMMapper {

    public MemberFCMJpaEntity toEntity(MemberFCM memberFCM) {
        return new MemberFCMJpaEntity(memberFCM.getId(),
                memberFCM.getMemberId(),
                memberFCM.getValue(),
                memberFCM.getCreatedAt(),
                memberFCM.getUpdatedAt());
    }

    public MemberFCM toDomain(MemberFCMJpaEntity memberFCMJpaEntity) {
        return MemberFCM.forMapper(memberFCMJpaEntity.getId(),
                memberFCMJpaEntity.getMemberId(),
                memberFCMJpaEntity.getValue(),
                memberFCMJpaEntity.getCreatedAt(),
                memberFCMJpaEntity.getUpdatedAt());
    }
}