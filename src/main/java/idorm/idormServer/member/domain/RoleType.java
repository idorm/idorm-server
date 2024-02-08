package idorm.idormServer.member.domain;

public enum RoleType {
    USER("USER"),
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