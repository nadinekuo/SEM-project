package gateway.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class JwtConfigTest {

    @Autowired
    private JwtConfig jwtConfig;

    @Test
    void getUri() {
        assertEquals("/eureka-security/**", jwtConfig.getUri());
    }

    @Test
    void getHeader() {
        assertEquals("Authorization", jwtConfig.getHeader());
    }

    @Test
    void getPrefix() {
        assertEquals("Bearer ", jwtConfig.getPrefix());
    }

    @Test
    void getExpiration() {
        assertEquals(86400, jwtConfig.getExpiration());
    }

    @Test
    void getSecret() {
        assertEquals("JwtSecretKey", jwtConfig.getSecret());
    }
}