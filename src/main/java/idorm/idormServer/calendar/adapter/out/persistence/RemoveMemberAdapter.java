package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.application.port.out.DeleteParticipantPort;
import idorm.idormServer.common.exception.CustomException;
import idorm.idormServer.common.exception.ExceptionCode;
import idorm.idormServer.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RemoveMemberAdapter implements DeleteParticipantPort {

    private final TeamRepository teamRepository;

    @Override
    public void deleteMember(Member member) {
        TeamJpaEntity team = teamRepository.findById(member.getId())
                .orElseThrow(() -> new CustomException(null, ExceptionCode.TEAM_NOT_FOUND));
        team.getMembers().remove(member);
    }
}
