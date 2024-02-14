package idorm.idormServer.calendar.domain;

import idorm.idormServer.common.util.Validator;
import lombok.Getter;

@Getter
public class Participant {

    private Long memberId;

    public Participant(final Long memberId){
        validateConstructor(memberId);
        this.memberId = memberId;
    }

    public static Participant forMapper(final Long memberId) {
        return new Participant(memberId);
    }

    private void validateConstructor(Long memberId) {
        Validator.validateNotNull(memberId);
    }
}