package idorm.idormServer.matchingInfo.domain;

import idorm.idormServer.exception.CustomException;
import lombok.Getter;

import static idorm.idormServer.exception.ExceptionCode.DORMCATEGORY_CHARACTER_INVALID;
import static idorm.idormServer.exception.ExceptionCode.SERVER_ERROR;

public enum DormCategory {

    DORM1('1'),
    DORM2('2'),
    DORM3('3');

    @Getter
    Character type;

    DormCategory(Character type) {
        this.type = type;
    }

    public static DormCategory validateType(String dormCategory) {
        try {
            return DormCategory.valueOf(dormCategory);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new CustomException(null, DORMCATEGORY_CHARACTER_INVALID);
        }
    }

    public static DormCategory valueOf(Character type) {
        DormCategory dormCategory = null;
        switch (type) {
            case '1':
                dormCategory = DormCategory.DORM1;
                break;
            case '2':
                dormCategory = DormCategory.DORM2;
                break;
            case '3':
                dormCategory = DormCategory.DORM3;
                break;
            default:
                throw new CustomException(null, SERVER_ERROR);
        }
        return dormCategory;
    }
}
