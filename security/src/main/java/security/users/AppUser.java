package security.users;

public class AppUser {

    private String username, password;
    private String role;

    /**
     * @param username
     * @param password
     * @param role
     */
    public AppUser(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * @return String Username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @Set Username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return String Password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @Set Password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return String Role
     */
    public String getRole() {
        return role;
    }

    /**
     * @Set Role
     */
    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUser appUser = (AppUser) o;
        return username.equals(appUser.username) && password.equals(appUser.password) && role.equals(appUser.role);
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
