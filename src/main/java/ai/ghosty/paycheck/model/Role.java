package ai.ghosty.paycheck.model;

public enum Role {
    ADMIN("admin"),
    USER("user"),;

    public final String roleTitle;

    Role(String roleTitle) {
        this.roleTitle = roleTitle;
    }
}
