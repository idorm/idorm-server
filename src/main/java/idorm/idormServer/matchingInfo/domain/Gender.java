package idorm.idormServer.matchingInfo.domain;

import static idorm.idormServer.common.exception.ExceptionCode.GENDER_CHARACTER_INVALID;

import idorm.idormServer.common.exception.CustomException;

public enum Gender {
    MALE,
    FEMALE;

    public static Gender validateType(String gender) {
        try {
            return Gender.valueOf(gender);
        } catch (IllegalArgumentException e) {
            throw new CustomException(null, GENDER_CHARACTER_INVALID);
        }
    }
}
