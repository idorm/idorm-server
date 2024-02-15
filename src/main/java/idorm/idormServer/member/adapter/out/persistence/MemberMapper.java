package idorm.idormServer.member.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Component;

import idorm.idormServer.matchingMate.adapter.out.persistence.MatchingMatesMapper;
import idorm.idormServer.member.domain.Member;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberMapper {

	private final MemberPhotoMapper memberPhotoMapper;
	private final NicknameMapper nicknameMapper;
	private final PasswordMapper passwordMapper;
	private final MatchingMatesMapper matchingMatesMapper;

	public MemberJpaEntity toEntity(Member member) {
		return new MemberJpaEntity(member.getId(),
			member.getMemberStatus(),
			member.getEmail(),
			nicknameMapper.toEntity(member.getNickname()),
			passwordMapper.toEntity(member.getPassword()),
			memberPhotoMapper.toEntity(member.getMemberPhoto()),
			member.getRoleType(),
			member.getCreatedAt(),
			member.getUpdatedAt(),
			matchingMatesMapper.toEntity(member.getMatchingMates()));
	}

	public List<MemberJpaEntity> toEntity(List<Member> members) {
		List<MemberJpaEntity> result = members.stream()
			.map(this::toEntity)
			.toList();
		return result;
	}

	public Member toDomain(MemberJpaEntity memberEntity) {
		return Member.forMapper(memberEntity.getId(),
			memberEntity.getMemberStatus(),
			memberEntity.getEmail(),
			nicknameMapper.toDomain(memberEntity.getNickname()),
			passwordMapper.toDomain(memberEntity.getPassword()),
			memberPhotoMapper.toDomain(memberEntity.getMemberPhoto()),
			memberEntity.getRoleType(),
			memberEntity.getCreatedAt(),
			memberEntity.getUpdatedAt(),
			matchingMatesMapper.toDomain(memberEntity.getMatchingMates()));
	}

	public List<Member> toDomain(List<MemberJpaEntity> entities) {
		List<Member> result = entities.stream()
			.map(this::toDomain)
			.toList();
		return result;
	}
}