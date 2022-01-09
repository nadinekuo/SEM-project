package user.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;

import org.junit.jupiter.api.Test;

class UserTest {

    private final transient User arslan;
    private final transient User emil;

    public UserTest() {
        arslan = new Customer("arslan123", "password1", true);
        emil = new Customer("emil123", "password2", false);
    }

    @Test
    void setUsernameTest() {
        emil.setUsername("emil234");
        assertThat(emil.getUsername()).isEqualTo("emil234");
    }

    @Test
    void setPasswordTest() {
        emil.setPassword("pwd123");
        assertThat(emil.getPassword()).isEqualTo("pwd123");
    }

    @Test
    void equalsTestTest() {
        User user = null;
        assertFalse(arslan.equals(user));
    }
}