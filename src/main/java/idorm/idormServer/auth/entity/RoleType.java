package idorm.idormServer.auth.entity;

public enum RoleType {
	MEMBER("MEMBER"),
	ADMIN("ADMIN");

	private String name;

	RoleType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean isNot(String roleType) {
		return !this.name.equals(roleType);
	}
}