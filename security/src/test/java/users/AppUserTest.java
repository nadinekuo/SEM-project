package users;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import security.users.AppUser;

public class AppUserTest {

    String username;
    String password;
    String role;
    AppUser appUser;

    @BeforeEach
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

    @Test
    void equalsTest() {
        AppUser appUserTest = new AppUser("emma", "emma123", "user");
        assertTrue(appUser.equals(appUserTest));
    }

    @Test
    void equalsSameTest() {
        assertTrue(appUser.equals(appUser));
    }

    @Test
    void notEqualsTest0() {
        AppUser appUserTest = new AppUser("erwin", "emma123", "user");
        assertFalse(appUser.equals(appUserTest));
    }

    @Test
    void notEqualsTest1() {
        AppUser appUserTest = new AppUser("emma", "emma12", "user");
        assertFalse(appUser.equals(appUserTest));
    }

    @Test
    void notEqualsTest2() {
        AppUser appUserTest = new AppUser("emma", "emma123", "admin");
        assertFalse(appUser.equals(appUserTest));
    }

    @Test
    void nullEqualsTest() {
        assertFalse(appUser.equals(null));
    }

    @Test
    void differentClassEqualsTest() {
        assertFalse(appUser.equals("test"));
    }

    @Test
    void toStringTest() {
        String check = "AppUser{" + "username='" + "emma" + '\'' + ", password='" + "emma123" + '\''
            + ", role='" + "user" + '\'' + '}';
        assertEquals(check, appUser.toString());
    }
}
