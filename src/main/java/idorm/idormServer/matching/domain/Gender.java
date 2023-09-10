package idorm.idormServer.matching.domain;

import idorm.idormServer.exception.CustomException;
import lombok.Getter;

import static idorm.idormServer.exception.ExceptionCode.GENDER_CHARACTER_INVALID;
import static idorm.idormServer.exception.ExceptionCode.SERVER_ERROR;

public enum Gender {
    MALE('M'),
    FEMALE('F');

    @Getter
    Character type;

    Gender(Character type) {
        this.type = type;
    }

    public static Gender validateType(String gender) {
        try {
            return Gender.valueOf(gender);
        } catch (IllegalArgumentException e) {
            throw new CustomException(null, GENDER_CHARACTER_INVALID);
        }
    }

    public static Gender valueOf(Character type) {
        Gender gender = null;
        switch (type) {
            case 'M':
                gender = Gender.MALE;
                break;
            case 'F':
                gender = Gender.FEMALE;
                break;
            default:
                throw new CustomException(null, SERVER_ERROR);
        }
        return gender;
    }
}
