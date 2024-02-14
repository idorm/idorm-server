package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.application.port.out.AddMemberPort;
import idorm.idormServer.calendar.domain.Team;
import idorm.idormServer.common.exception.CustomException;
import idorm.idormServer.common.exception.ExceptionCode;
import idorm.idormServer.member.adapter.out.persistence.MemberEntity;
import idorm.idormServer.member.adapter.out.persistence.MemberRepository;
import idorm.idormServer.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddMemberAdapter implements AddMemberPort {
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final TeamMapper teamMapper;
    @Override
    public Team addMember(Member member) {
        MemberEntity memberEntity = memberRepository.findById(member.getId())
                .orElseThrow(() -> new CustomException(null, ExceptionCode.MEMBER_NOT_FOUND));

        TeamJpaEntity teamJpaEntity = teamRepository.findById(member.getId())
                .orElseThrow(() -> new CustomException(null, ExceptionCode.TEAM_NOT_FOUND));

        teamJpaEntity.getMembers().add(memberEntity);

        return teamMapper.toDomain(teamJpaEntity);
    }
}
