package idorm.idormServer.matching.domain;

import idorm.idormServer.exception.CustomException;
import lombok.Getter;

import static idorm.idormServer.exception.ExceptionCode.JOINPERIOD_CHARACTER_INVALID;
import static idorm.idormServer.exception.ExceptionCode.SERVER_ERROR;

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
            throw new CustomException(null, JOINPERIOD_CHARACTER_INVALID);
        }
    }

    public static JoinPeriod valueOf(Character type) {
        JoinPeriod joinPeriod = null;
        switch (type) {
            case '1':
                joinPeriod = JoinPeriod.WEEK16;
                break;
            case '2':
                joinPeriod = JoinPeriod.WEEK24;
                break;
            default:
                throw new CustomException(null, SERVER_ERROR);
        }
        return joinPeriod;
    }
}
