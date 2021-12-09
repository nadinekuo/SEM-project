package user.config;

import java.io.Serializable;

public class UserDtoConfig implements Serializable {

    private static final long serialVersionUID = 42L;

    private String username;

    private String password;

    private boolean premiumSubscription;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isPremiumSubscription() {
        return premiumSubscription;
    }

    public void setPremiumSubscription(boolean premiumSubscription) {
        this.premiumSubscription = premiumSubscription;
    }

}

