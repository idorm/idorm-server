package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.application.port.out.LoadTeamPort;
import idorm.idormServer.calendar.domain.Team;
import idorm.idormServer.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoadTeamAdapter implements LoadTeamPort {

    @Override
    public Team load(Member member) {
        return member.getTeam().isTeamExists();
    }

}
