package services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import security.services.AuthenticationService;

import static org.junit.Assert.assertNotNull;

/**
 * The type Authentication service test.
 */
@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    private transient AuthenticationService authenticationService;

    /**
     * Sets test attributes.
     */
    @BeforeEach
    void setup() {
        authenticationService = new AuthenticationService();
    }

    @Test
    public void constructorTest() {
        assertNotNull(authenticationService);
    }

    @Test
    public void restTemplateTest() {
        assertNotNull(authenticationService.restTemplate());
    }
}
