package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import security.controllers.AuthenticationController;
import security.services.AuthenticationService;
import security.users.AppUser;

/**
 * The type Authentication Controller test.
 */
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@MockitoSettings(strictness = Strictness.LENIENT)
public class AuthenticationControllerTest {

    private static final String userUrl = "http://eureka-user";

    AuthenticationController authenticationController;

    @Mock
    transient AuthenticationService authenticationService;

    @Mock
    transient RestTemplate restTemplate;

    @Autowired
    private transient MockMvc mockMvc;

    transient List<String> userInfo;

    transient List<String> adminInfo;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup() {
        Mockito.when(authenticationService.restTemplate()).thenReturn(restTemplate);
        authenticationController = new AuthenticationController(restTemplate);
        userInfo = new ArrayList<>();
        userInfo.add("emma");
        userInfo.add("password");
        userInfo.add("user");

        adminInfo = new ArrayList<>();
        adminInfo.add("emmaAdmin");
        adminInfo.add("password");
        adminInfo.add("admin");
    }

    @Test
    void constructorTest() {
        assertNotNull(authenticationController);
    }

    @Test
    void getCustomerInfoTest() {
        Mockito.when(restTemplate.getForEntity(anyString(), any()))
                .thenReturn(ResponseEntity.of(Optional.of(userInfo)));
        AppUser check = new AppUser("emma", "password", "user");
        assertEquals(check, authenticationController.getCustomerInfo("emma"));
    }

    @Test
    void getAdminInfoTest() {
        Mockito.when(restTemplate.getForEntity(anyString(), any()))
                .thenReturn(ResponseEntity.of(Optional.of(adminInfo)));
        AppUser check = new AppUser("emmaAdmin", "password", "admin");
        assertEquals(check, authenticationController.getAdminInfo("emmaAdmin"));
    }

    @Test
    void getCustomerInfoFalseTest() {
        Mockito.when(restTemplate.getForEntity(anyString(), any()))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        assertEquals(null, authenticationController.getCustomerInfo("emmaFalse"));
    }

    @Test
    void getAdminInfoFalseTest() {
        Mockito.when(restTemplate.getForEntity(anyString(), any()))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        assertEquals(null, authenticationController.getAdminInfo("emmaAdminFalse"));
    }

}
