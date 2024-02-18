package idorm.idormServer.matchingMate.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Component;

import idorm.idormServer.matchingMate.domain.MatchingMate;
import idorm.idormServer.member.adapter.out.persistence.MemberMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingMateMapper {

	private final MemberMapper memberMapper;

	public MatchingMateJpaEntity toEntity(MatchingMate matchingMate) {
		return new MatchingMateJpaEntity(matchingMate.getId(),
			memberMapper.toEntity(matchingMate.getMember()),
			memberMapper.toEntity(matchingMate.getTargetMember()),
			matchingMate.getPreferenceType());
	}

	public List<MatchingMateJpaEntity> toEntity(List<MatchingMate> matchingMates) {
		List<MatchingMateJpaEntity> result = matchingMates.stream()
			.map(this::toEntity)
			.toList();
		return result;
	}

	public MatchingMate toDomain(MatchingMateJpaEntity matchingMateEntity) {
		return MatchingMate.forMapper(matchingMateEntity.getId(),
			memberMapper.toDomain(matchingMateEntity.getMember()),
			memberMapper.toDomain(matchingMateEntity.getTargetMember()),
			matchingMateEntity.getPreferenceType());
	}

	public List<MatchingMate> toDomain(List<MatchingMateJpaEntity> entities) {
		List<MatchingMate> result = entities.stream()
			.map(this::toDomain)
			.toList();
		return result;
	}
}