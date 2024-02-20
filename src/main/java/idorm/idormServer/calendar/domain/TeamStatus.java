package idorm.idormServer.calendar.domain;

public enum TeamStatus {
	ACTIVE, ALONE;

	public static boolean isNotAlone(TeamStatus teamStatus) {
		return !teamStatus.equals(ALONE);
	}

	public static boolean isAlone(TeamStatus teamStatus) {
		return teamStatus.equals(ALONE);
	}
}