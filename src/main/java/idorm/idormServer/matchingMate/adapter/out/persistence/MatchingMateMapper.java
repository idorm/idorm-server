package idorm.idormServer.matchingMate.adapter.out.persistence;

import idorm.idormServer.matchingMate.domain.MatchingMate;
import idorm.idormServer.member.adapter.out.persistence.MemberMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingMateMapper {

    private final MemberMapper memberMapper;

    public MatchingMateJpaEntity toEntity(MatchingMate matchingMate) {
        return new MatchingMateJpaEntity(memberMapper.toEntity(matchingMate.getMember()),
                memberMapper.toEntity(matchingMate.getTargetMember()));
    }

    public MatchingMate toDomain(MatchingMateJpaEntity matchingMateEntity) {
        return MatchingMate.forMapper(memberMapper.toDomain(matchingMateEntity.getMember()),
                memberMapper.toDomain(matchingMateEntity.getTargetMember()));
    }
}