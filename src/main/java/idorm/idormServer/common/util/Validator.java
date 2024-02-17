package idorm.idormServer.common.util;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.common.exception.BaseResponseCode;
import idorm.idormServer.common.exception.FieldRequiredException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class Validator {

	public static void validateNotBlank(String value) {
		if (value == null || value.isBlank()) {
			throw new FieldRequiredException();
		}
	}

	public static void validateNotBlank(List<String> values) {
		values.forEach(Validator::validateNotBlank);
	}

	public static void validateNotNull(Object object) {
		if (object == null) {
			throw new FieldRequiredException();
		}
	}

	public static void validateNotNull(List<Object> objects) {
		objects.forEach(Validator::validateNotNull);
	}

	public static void validateLength(String value, int min, int max,
		BaseResponseCode baseResponseCode) {
		if (value.length() < min || value.length() > max) {
			throw new BaseException(baseResponseCode);
		}
	}

	public static void validateSize(Integer value, int min, int max,
		BaseResponseCode baseResponseCode) {
		if (value < min || value > max) {
			throw new BaseException(baseResponseCode);
		}
	}

	public static void validateMaxLength(String value, int max, BaseResponseCode baseResponseCode) {
		if (Objects.isNull(value)) {
			return;
		}

		if (value.length() > max) {
			throw new BaseException(baseResponseCode);
		}
	}

	public static void validateFormat(String value, String regex, BaseResponseCode baseResponseCode) {
		if (!Pattern.matches(regex, value)) {
			throw new BaseException(baseResponseCode);
		}
	}
}