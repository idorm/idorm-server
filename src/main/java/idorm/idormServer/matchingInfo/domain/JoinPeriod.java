package idorm.idormServer.matchingInfo.domain;

import static idorm.idormServer.common.exception.ExceptionCode.JOINPERIOD_CHARACTER_INVALID;

import idorm.idormServer.common.exception.CustomException;

public enum JoinPeriod {
    WEEK16,
    WEEK24;

    public static JoinPeriod validateType(String joinPeriod) {
        try {
            return JoinPeriod.valueOf(joinPeriod);
        } catch (IllegalArgumentException e) {
            throw new CustomException(null, JOINPERIOD_CHARACTER_INVALID);
        }
    }
}
