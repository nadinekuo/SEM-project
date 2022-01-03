package users;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import security.users.AppUser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AppUserTest {

    String username;
    String password;
    String role;
    AppUser appUser;

    @BeforeAll
    void setup() {
        username = "emma";
        password = "emma123";
        role = "user";
        appUser = new AppUser(username, password, role);
    }

    @Test
    void constructorTest() {
        assertNotNull(appUser);
    }

    @Test
    void getUsernameTest() {
        assertEquals("emma", appUser.getUsername());
    }

    @Test
    void setUsernameTest() {
        appUser.setUsername("emma1");
        assertEquals("emma1", appUser.getUsername());
    }

    @Test
    void getPasswordTest() {
        assertEquals("emma123", appUser.getPassword());
    }

    @Test
    void setPasswordTest() {
        appUser.setPassword("password");
        assertEquals("password", appUser.getPassword());
    }

    @Test
    void getRoleTest() {
        assertEquals("user", appUser.getRole());
    }

    @Test
    void setRoleTest() {
        appUser.setRole("admin");
        assertEquals("admin", appUser.getRole());
    }
}
