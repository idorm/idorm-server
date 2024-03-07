package idorm.idormServer.notification.adapter.out.persistence;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import idorm.idormServer.auth.entity.RoleType;
import idorm.idormServer.matchingInfo.entity.DormCategory;
import idorm.idormServer.notification.application.port.out.LoadFcmTokenPort;
import idorm.idormServer.notification.entity.FcmToken;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoadFcmTokenAdapter implements LoadFcmTokenPort {

	private final FcmTokenRepository fcmTokenRepository;

	@Override
	public Optional<FcmToken> load(final Long memberId) {
		return fcmTokenRepository.findByMemberId(memberId);
	}

	@Override
	public List<FcmToken> loadAllByDormCategory(final DormCategory dormCategory) {
		List<FcmToken> responses = fcmTokenRepository.findByDormCategory(dormCategory);
		return responses != null ? responses : Collections.emptyList();
	}

	@Override
	public List<FcmToken> loadAllByMemberIds(List<Long> memberIds) {
		List<FcmToken> responses = fcmTokenRepository.findByMemberIds(memberIds);
		return responses != null ? responses : Collections.emptyList();
	}

	@Override
	public List<FcmToken> loadAllByAdmins() {
		List<FcmToken> responses = fcmTokenRepository.findByAdmins(RoleType.ADMIN);
		return responses != null ? responses : Collections.emptyList();
	}
}
