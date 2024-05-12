package idorm.idormServer.calendar.entity;

public enum TeamStatus {
	ACTIVE,
	ALONE,
	DELETED;

	public boolean isNotAlone() {
		return !this.equals(ALONE);
	}

	public boolean isAlone() {
		return this.equals(ALONE);
	}
}