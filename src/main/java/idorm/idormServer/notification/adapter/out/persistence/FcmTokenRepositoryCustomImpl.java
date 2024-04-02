package idorm.idormServer.notification.adapter.out.persistence;

import static idorm.idormServer.auth.entity.RoleType.*;
import static idorm.idormServer.matchingInfo.entity.QMatchingInfo.*;
import static idorm.idormServer.member.entity.QMember.*;
import static idorm.idormServer.notification.entity.QFcmToken.*;

import java.util.List;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import idorm.idormServer.auth.entity.RoleType;
import idorm.idormServer.matchingInfo.entity.DormCategory;
import idorm.idormServer.notification.entity.FcmToken;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FcmTokenRepositoryCustomImpl implements FcmTokenRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<FcmToken> findByDormCategory(DormCategory dormCategory) {
		return queryFactory
			.selectFrom(fcmToken)
			.leftJoin(member).on(fcmToken.memberId.eq(member.id))
			.leftJoin(matchingInfo).on(member.matchingInfo.eq(matchingInfo))
			.where(matchingInfo.dormInfo.dormCategory.eq(dormCategory))
			.fetch();
	}

	@Override
	public List<FcmToken> findByAdmins(RoleType roleType) {
		return queryFactory
			.selectFrom(fcmToken)
			.leftJoin(member).on(fcmToken.memberId.eq(member.id))
			.where(roleEq(ADMIN))
			.fetch();
	}

	private BooleanExpression roleEq(RoleType roleType) {
		return member.roleType.eq(roleType);
	}
}
