package idorm.idormServer.notification.adapter.out.persistence;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import idorm.idormServer.auth.entity.RoleType;
import idorm.idormServer.matchingInfo.entity.DormCategory;
import idorm.idormServer.notification.entity.FcmToken;

@Repository
public interface FcmTokenRepositoryCustom {

	List<FcmToken> findByDormCategory(@Param("dormCategory") DormCategory dormCategory);

	List<FcmToken> findByAdmins(@Param("roleType") RoleType roleType);
}