package idorm.idormServer.member.adapter.out.persistence;

import idorm.idormServer.matchingMate.adapter.out.persistence.MatchingMatesMapper;
import idorm.idormServer.member.domain.Member;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberMapper {

    private final MemberPhotoMapper memberPhotoMapper;
    private final NicknameMapper nicknameMapper;
    private final PasswordMapper passwordMapper;
    private final MatchingMatesMapper matchingMatesMapper;

    public MemberJpaEntity toEntity(Member member) {
        return MemberJpaEntity.builder()
                .email(member.getEmail())
                .nickname(nicknameMapper.toEntity(member.getNickname()))
                .password(passwordMapper.toEntity(member.getPassword()))
                .memberPhoto(memberPhotoMapper.toEntity(member.getMemberPhoto()))
                .roleType(member.getRoleType())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .matchingMates(matchingMatesMapper.toEntity(member.getMatchingMates()))
                .build();
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
}