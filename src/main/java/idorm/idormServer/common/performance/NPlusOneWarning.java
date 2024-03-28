package idorm.idormServer.common.performance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NPlusOneWarning {
	private static final int WARNING_STANDARD_COUNT = 2;
	private static final String WARNING_STANDARD_WARDING = "select";
	private static final String WARNING_MESSAGE = "\n동일한 select 쿼리문이 2개 이상 발생합니다. \nN+1 문제 혹은 로직 개선이 필요해 보입니다.";
	private static final String LINE_BREAK = "\n\n";

	public static String getWarningMessage(List<String> sql) {
		if (isWarning(sql)) {
			return WARNING_MESSAGE + LINE_BREAK;
		}
		return LINE_BREAK;
	}

	private static boolean isWarning(List<String> sql) {
		final Map<String, Integer> nPlusOneWaring = createNPlusOneWaring(sql);
		for (Integer value : nPlusOneWaring.values()) {
			if (value > WARNING_STANDARD_COUNT) {
				return true;
			}
		}
		return false;
	}

	private static Map<String, Integer> createNPlusOneWaring(final List<String> sqlQueries) {
		final Map<String, Integer> nPlusOneWaring = new HashMap<>();
		for (String query : sqlQueries) {
			if (query.contains(WARNING_STANDARD_WARDING)) {
				nPlusOneWaring.put(query, nPlusOneWaring.getOrDefault(query, 0) + 1);
			}
		}
		return nPlusOneWaring;
	}
}
