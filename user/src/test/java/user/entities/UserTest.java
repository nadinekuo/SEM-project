package user.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UserTest {

    private final transient User arslan;
    private final transient User emil;

    public UserTest() {
        arslan = new Customer("arslan123", "password1", true);
        emil = new Customer("emil123", "password2", false);
    }


    @Test
    void getUsername() {
        assertThat(arslan.getUsername()).isEqualTo("arslan123");
    }

    @Test
    void setUsername() {
        emil.setUsername("emil234");
        assertThat(emil.getUsername()).isEqualTo("emil234");
    }

    @Test
    void getPassword() {
        assertThat(arslan.getPassword()).isEqualTo("password1");
    }

    @Test
    void setPassword() {
        emil.setPassword("pwd123");
        assertThat(emil.getPassword()).isEqualTo("pwd123");
    }
}