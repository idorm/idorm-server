package idorm.idormServer.matchingInfo.domain;

import idorm.idormServer.exception.CustomException;
import lombok.Getter;

import static idorm.idormServer.exception.ExceptionCode.JOINPERIOD_CHARACTER_INVALID;

public enum JoinPeriod {

    WEEK16('1'),
    WEEK24('2');

    @Getter
    Character type;

    JoinPeriod(Character type) {
        this.type = type;
    }

    public static JoinPeriod validateType(String joinPeriod) {
        try {
            return JoinPeriod.valueOf(joinPeriod);
        } catch (IllegalArgumentException e) {
            throw new CustomException(JOINPERIOD_CHARACTER_INVALID);
        }
    }

    public static JoinPeriod valueOf(Character type) {
        JoinPeriod joinPeriod = null;
        switch (type) {
            case '1':
                joinPeriod = JoinPeriod.WEEK16;
                break;
            case 'F':
                joinPeriod = JoinPeriod.WEEK24;
                break;
            default:
                break;
        }
        return joinPeriod;
    }
}
