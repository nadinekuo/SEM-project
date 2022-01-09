package user.entities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AdminTest {

    private final transient Admin admin;

    public AdminTest() {
        admin = new Admin(1L, "admin1", "adminpwd");
    }

    @Test
    public void toStringTest() {
        String result = "Admin{id=1, username='admin1', password='adminpwd'}";
        assertThat(result).isEqualTo(admin.toString());
    }

}