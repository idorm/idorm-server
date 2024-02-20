package idorm.idormServer.notification.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Component;

import idorm.idormServer.notification.domain.FcmToken;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmTokenMapper {

	public FcmTokenJpaEntity toEntity(FcmToken memberFCM) {
		return new FcmTokenJpaEntity(memberFCM.getId(),
			memberFCM.getMemberId(),
			memberFCM.getValue(),
			memberFCM.getCreatedAt(),
			memberFCM.getUpdatedAt());
	}

	public List<FcmTokenJpaEntity> toEntity(List<FcmToken> memberFCMs) {
		List<FcmTokenJpaEntity> result = memberFCMs.stream()
			.map(this::toEntity)
			.toList();
		return result;
	}

	public FcmToken toDomain(FcmTokenJpaEntity memberFCMJpaEntity) {
		return FcmToken.forMapper(memberFCMJpaEntity.getId(),
			memberFCMJpaEntity.getMemberId(),
			memberFCMJpaEntity.getValue(),
			memberFCMJpaEntity.getCreatedAt(),
			memberFCMJpaEntity.getUpdatedAt());
	}

	public List<FcmToken> toDomain(List<FcmTokenJpaEntity> entities) {
		List<FcmToken> result = entities.stream()
			.map(this::toDomain)
			.toList();
		return result;
	}
}