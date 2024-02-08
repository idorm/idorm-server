package idorm.idormServer.common.util;

import static idorm.idormServer.common.exception.ExceptionCode.FIELD_REQUIRED;

import idorm.idormServer.common.exception.CustomException;
import idorm.idormServer.common.exception.ExceptionCode;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class Validator {

    public static void validateNotBlank(String value) {
        if (value == null || value.isBlank()) {
            throw new CustomException(null, FIELD_REQUIRED);
        }
    }

    public static void validateNotBlank(List<String> values) {
        values.forEach(Validator::validateNotBlank);
    }

    public static void validateNotNull(Object object) {
        if (object == null) {
            throw new CustomException(null, FIELD_REQUIRED);
        }
    }

    public static void validateNotNull(List<Object> objects) {
        objects.forEach(Validator::validateNotNull);
    }

    public static void validateLength(String value, int min, int max, ExceptionCode exceptionCode) {
        if (value.length() < min || value.length() > max) {
            throw new CustomException(null, exceptionCode);
        }
    }

    public static void validateSize(Integer value, int min, int max, ExceptionCode exceptionCode) {
        if (value < min || value > max) {
            throw new CustomException(null, exceptionCode);
        }
    }

    public static void validateMaxLength(String value, int max, ExceptionCode exceptionCode) {
        if (Objects.isNull(value)) {
            return;
        }

        if (value.length() > max) {
            throw new CustomException(null, exceptionCode);
        }
    }

    public static void validateFormat(String value, String regex, ExceptionCode exceptionCode) {
        if (!Pattern.matches(regex, value)) {
            throw new CustomException(null, exceptionCode);
        }
    }
}