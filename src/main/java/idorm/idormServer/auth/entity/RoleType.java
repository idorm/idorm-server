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

	public static boolean isNotAdmin(String roleType) {
		return !ADMIN.equals(roleType);
	}
}