package idorm.idormServer.matchingInfo.domain;

import lombok.Getter;

public enum Gender {
    MALE('M'),
    FEMALE('F');

    @Getter
    Character type;

    Gender(Character type) {
        this.type = type;
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
