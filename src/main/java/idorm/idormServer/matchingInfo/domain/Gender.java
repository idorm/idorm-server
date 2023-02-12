package idorm.idormServer.matchingInfo.domain;

import idorm.idormServer.exception.CustomException;
import lombok.Getter;

import static idorm.idormServer.exception.ExceptionCode.GENDER_CHARACTER_INVALID;

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
            throw new CustomException(GENDER_CHARACTER_INVALID);
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
                break;
        }
        return gender;
    }
}
