package ai.ghosty.paycheck.model;

public class User {
    private int id; // foreign key. points to an employee entity.
    private String username;
    private String passwordHash;
    private String role;
    private Employee employee;

    public User (int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.passwordHash = password;
        this.role = role;
    }

    public int getId() {return id;}

    public String getUsername() { return username; }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getRole() {
        return role;
    }
}
