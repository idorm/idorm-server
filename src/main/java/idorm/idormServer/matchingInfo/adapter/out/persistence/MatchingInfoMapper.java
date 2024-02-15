package idorm.idormServer.matchingInfo.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Component;

import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import idorm.idormServer.member.adapter.out.persistence.MemberMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingInfoMapper {

	private final MemberMapper memberMapper;
	private final DormInfoMapper dormInfoMapper;
	private final PreferenceInfoMapper preferenceInfoMapper;
	private final TextInfoMapper textInfoMapper;
	private final SharedURLMapper sharedURLMapper;

	public MatchingInfoJpaEntity toEntity(MatchingInfo matchingInfo) {
		return new MatchingInfoJpaEntity(matchingInfo.getId(),
			memberMapper.toEntity(matchingInfo.getMember()),
			dormInfoMapper.toEntity(matchingInfo.getDormInfo()),
			preferenceInfoMapper.toEntity(matchingInfo.getPreferenceInfo()),
			textInfoMapper.toEntity(matchingInfo.getTextInfo()),
			sharedURLMapper.toEntity(matchingInfo.getSharedURL()),
			matchingInfo.getIsPublic());
	}

	public List<MatchingInfoJpaEntity> toEntity(List<MatchingInfo> matchingInfos) {
		List<MatchingInfoJpaEntity> result = matchingInfos.stream()
			.map(this::toEntity)
			.toList();
		return result;
	}

	public MatchingInfo toDomain(MatchingInfoJpaEntity matchingInfoEntity) {
		return MatchingInfo.forMapper(matchingInfoEntity.getId(),
			memberMapper.toDomain(matchingInfoEntity.getMember()),
			dormInfoMapper.toDomain(matchingInfoEntity.getDormInfo()),
			preferenceInfoMapper.toDomain(matchingInfoEntity.getPreferenceInfo()),
			textInfoMapper.toDomain(matchingInfoEntity.getTextInfo()),
			sharedURLMapper.toDomain(matchingInfoEntity.getSharedURL()),
			matchingInfoEntity.getIsPublic());
	}

	public List<MatchingInfo> toDomain(List<MatchingInfoJpaEntity> entities) {
		List<MatchingInfo> result = entities.stream()
			.map(this::toDomain)
			.toList();
		return result;
	}
}