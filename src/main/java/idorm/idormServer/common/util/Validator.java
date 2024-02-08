package idorm.idormServer.common.util;

import static idorm.idormServer.common.exception.ExceptionCode.FIELD_REQUIRED;

import idorm.idormServer.common.exception.CustomException;
import idorm.idormServer.common.exception.ExceptionCode;
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

    public static void validateNotNull(Object object, ExceptionCode exceptionCode) {
        if (object == null) {
            throw new CustomException(null, exceptionCode);
        }
    }

    public static void validateFormat(String value, String regex, ExceptionCode exceptionCode) {
        if (!Pattern.matches(regex, value)) {
            throw new CustomException(null, exceptionCode);
        }
    }
}