package idorm.idormServer.report.domain;

import idorm.idormServer.exception.CustomException;
import lombok.Getter;

import static idorm.idormServer.exception.ExceptionCode.MEMBER_REPORT_TYPE_CHARACTER_INVALID;
import static idorm.idormServer.exception.ExceptionCode.SERVER_ERROR;

public enum MemberReason {

    NICKNAME('N'),
    PROFILE_PHOTO('P'),
    MATCHINGINFO('M'),
    ETC('E');

    @Getter
    Character type;

    MemberReason(Character type) {
        this.type = type;
    }

    public  static MemberReason validateType(String memberReason) {
        try {
            return MemberReason.valueOf(memberReason);
        } catch (IllegalArgumentException e) {
            throw new CustomException(MEMBER_REPORT_TYPE_CHARACTER_INVALID);
        }
    }

    public static MemberReason valueOf(Character type) {
        MemberReason memberReason = null;
        switch (type) {
            case 'N':
                memberReason = MemberReason.NICKNAME;
                break;
            case 'P':
                memberReason = MemberReason.PROFILE_PHOTO;
                break;
            case 'M':
                memberReason = MemberReason.MATCHINGINFO;
                break;
            case 'E':
                memberReason = MemberReason.ETC;
                break;
            default:
                throw new CustomException(SERVER_ERROR);
        }
        return memberReason;
    }
}
