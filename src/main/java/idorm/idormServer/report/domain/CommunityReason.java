package idorm.idormServer.report.domain;

import idorm.idormServer.common.exception.CustomException;
import lombok.Getter;

import static idorm.idormServer.common.exception.ExceptionCode.*;

public enum CommunityReason {

    PLASTER('P'), // 도배
    ADVERTISING_SPAM('S'), // 광고 / 스팸
    OBSCENE_MATERIAL('O'), // 음란물 / 선정성
    ABUSE('A'), // 욕설
    FALSE_INFORMATION('F'), // 사칭 / 허위
    ETC('E');

    @Getter
    Character type;

    CommunityReason(Character type) {
        this.type = type;
    }

    public static CommunityReason validateType(String communityReason) {
        try {
            return CommunityReason.valueOf(communityReason);
        } catch (IllegalArgumentException e) {
            throw new CustomException(null, COMMUNITY_REPORT_TYPE_CHARACTER_INVALID);
        }
    }

    public static CommunityReason valueOf(Character type) {
        CommunityReason communityReason = null;
        switch (type) {
            case 'P':
                communityReason = CommunityReason.PLASTER;
                break;
            case 'S':
                communityReason = CommunityReason.ADVERTISING_SPAM;
                break;
            case 'O':
                communityReason = CommunityReason.OBSCENE_MATERIAL;
                break;
            case 'A':
                communityReason = CommunityReason.ABUSE;
                break;
            case 'F':
                communityReason = CommunityReason.FALSE_INFORMATION;
                break;
            case 'E':
                communityReason = CommunityReason.ETC;
                break;
            default:
                throw new CustomException(null, SERVER_ERROR);
        }
        return communityReason;
    }
}
