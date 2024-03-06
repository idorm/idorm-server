package idorm.idormServer.notification.application.port.out;

import java.util.List;
import java.util.Optional;

import idorm.idormServer.matchingInfo.entity.DormCategory;
import idorm.idormServer.notification.entity.FcmToken;

public interface LoadFcmTokenPort {

	Optional<FcmToken> load(Long memberId);

	List<FcmToken> loadAllByDormCategory(DormCategory dormCategory);

	List<FcmToken> loadAllByMemberIds(List<Long> memberIds);

	List<FcmToken> loadAllByAdmins();
}
