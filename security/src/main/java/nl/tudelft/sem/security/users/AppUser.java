package nl.tudelft.sem.security.users;

public class AppUser {

    private String username;
    private String password;
    private String role;

    /**
     * Constructor for app user.
     *
     * @param username username
     * @param password password
     * @param role     user's role, customer or admin
     */
    public AppUser(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * Getter for username.
     *
     * @return String Username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for username.
     *
     * @Set Username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter for password.
     *
     * @return String Password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for password.
     *
     * @Set Password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter for role.
     *
     * @return String Role
     */
    public String getRole() {
        return role;
    }

    /**
     * Setter for role.
     *
     * @Set Role
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Equals method.
     *
     * @param o object
     * @return result
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AppUser appUser = (AppUser) o;
        return username.equals(appUser.username) && password.equals(appUser.password) && role
            .equals(appUser.role);
    }

    /**
     * To string method.
     *
     * @return string
     */
    @Override
    public String toString() {
        return "AppUser{" + "username='" + username + '\'' + ", password='" + password + '\''
            + ", role='" + role + '\'' + '}';
    }
}
