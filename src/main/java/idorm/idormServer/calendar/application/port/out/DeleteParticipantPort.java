package idorm.idormServer.calendar.application.port.out;

import idorm.idormServer.member.domain.Member;

public interface DeleteParticipantPort {
    void deleteMember(Member member);
}
