package nl.tudelft.sem.security.authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = JwtConfig.class)
@SpringBootTest
class JwtConfigTest {

    @Autowired
    private JwtConfig jwtConfig;

    @Test
    void getUriTest() {
        assertEquals("/eureka-security/**", jwtConfig.getUri());
    }

    @Test
    void getHeaderTest() {
        assertEquals("Authorization", jwtConfig.getHeader());
    }

    @Test
    void getPrefixTest() {
        assertEquals("Bearer ", jwtConfig.getPrefix());
    }

    @Test
    void getExpirationTest() {
        assertEquals(86400, jwtConfig.getExpiration());
    }

    @Test
    void getSecretTest() {
        assertEquals("JwtSecretKey", jwtConfig.getSecret());
    }
}