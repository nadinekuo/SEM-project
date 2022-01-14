package nl.tudelft.sem.user.config;

import java.io.Serializable;

public class UserDtoConfig implements Serializable {

    private static final long serialVersionUID = 42L;

    private String username;

    private String password;

    private boolean premiumSubscription;

    /**
     * Instantiates a new User dto config.
     */
    public UserDtoConfig() {
    }

    /**
     * Instantiates a new User dto config.
     *
     * @param username            the username
     * @param password            the password
     * @param premiumSubscription if it's a premium subscription
     */
    public UserDtoConfig(String username, String password, boolean premiumSubscription) {
        this.username = username;
        this.password = password;
        this.premiumSubscription = premiumSubscription;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Is premium subscription boolean.
     *
     * @return the boolean
     */
    public boolean isPremiumSubscription() {
        return premiumSubscription;
    }

    /**
     * Sets premium subscription.
     *
     * @param premiumSubscription the premium subscription
     */
    public void setPremiumSubscription(boolean premiumSubscription) {
        this.premiumSubscription = premiumSubscription;
    }

    @Override
    public String toString() {
        return "UserDtoConfig{" + "username='" + username + '\'' + ", password='" + password + '\''
            + ", premiumSubscription=" + premiumSubscription + '}';
    }
}

