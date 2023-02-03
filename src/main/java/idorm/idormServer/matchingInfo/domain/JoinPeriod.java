package idorm.idormServer.matchingInfo.domain;

import lombok.Getter;

public enum JoinPeriod {

    WEEK16('1'),
    WEEK24('2');

    @Getter
    Character type;

    JoinPeriod(Character type) {
        this.type = type;
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
