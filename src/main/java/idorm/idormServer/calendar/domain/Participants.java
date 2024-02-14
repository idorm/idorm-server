package idorm.idormServer.calendar.domain;

import idorm.idormServer.common.exception.CustomException;
import idorm.idormServer.common.exception.ExceptionCode;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;

@Getter
public class Participants {

    private Set<Participant> participants = new HashSet<>();

    public Participants(final Set<Participant> participants){
        this.participants = participants;
    }

    public static Participants forMapper(final Set<Participant> participants) {
        return new Participants(participants);
    }

    void participate(Long memberId) {
        if (isParticipationIn(memberId)) {
            throw new CustomException(null, ExceptionCode.DUPLICATE_MEMBER);
        }

        participants.add(new Participant(memberId));
    }

    void delete(Long memberId) {
        if(!participants.contains(new Participant(memberId))){
            throw new CustomException(null, ExceptionCode.MEMBER_NOT_FOUND);
        }
        participants.remove(new Participant(memberId));
    }

    boolean isParticipationIn(Long memberId) {
        return participants.contains(new Participant(memberId));
    }
}