package idorm.idormServer.member.entity;

public enum MemberStatus {
	ACTIVE,
	DELETED;

	public boolean isDeleted() {
		return this.equals(DELETED);
	}
}