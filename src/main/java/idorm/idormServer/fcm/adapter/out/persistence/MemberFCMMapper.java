package idorm.idormServer.fcm.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Component;

import idorm.idormServer.fcm.domain.MemberFCM;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

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

	public List<MemberFCMJpaEntity> toEntity(List<MemberFCM> memberFCMs) {
		List<MemberFCMJpaEntity> result = memberFCMs.stream()
			.map(this::toEntity)
			.toList();
		return result;
	}

	public MemberFCM toDomain(MemberFCMJpaEntity memberFCMJpaEntity) {
		return MemberFCM.forMapper(memberFCMJpaEntity.getId(),
			memberFCMJpaEntity.getMemberId(),
			memberFCMJpaEntity.getValue(),
			memberFCMJpaEntity.getCreatedAt(),
			memberFCMJpaEntity.getUpdatedAt());
	}

	public List<MemberFCM> toDomain(List<MemberFCMJpaEntity> entities) {
		List<MemberFCM> result = entities.stream()
			.map(this::toDomain)
			.toList();
		return result;
	}
}