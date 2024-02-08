package idorm.idormServer.matchingInfo.domain;

import static idorm.idormServer.common.exception.ExceptionCode.DORMCATEGORY_CHARACTER_INVALID;

import idorm.idormServer.common.exception.CustomException;

public enum DormCategory {
    DORM1,
    DORM2,
    DORM3;

    public static DormCategory validateType(String dormCategory) {
        try {
            return DormCategory.valueOf(dormCategory);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new CustomException(null, DORMCATEGORY_CHARACTER_INVALID);
        }
    }
}
