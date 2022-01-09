package security.authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = JwtConfig.class)
@SpringBootTest
class JwtUsernameAndPasswordAuthenticationFilterTest {

    @Autowired
    private JwtConfig jwtConfig;

    @MockBean
    private AuthenticationManager authManager;

    @Test
    public void attemptAuthentication() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("username", "erwin");
        object.put("password", "randomstring");
        JwtUsernameAndPasswordAuthenticationFilter filter =
            new JwtUsernameAndPasswordAuthenticationFilter(authManager, jwtConfig);
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/eureka-security");
        request.setContentType("application/json");
        request.setContent(object.toString().getBytes(StandardCharsets.UTF_8));
        Authentication mock = mock(Authentication.class);
        when(authManager.authenticate(any())).thenReturn(mock);
        assertNotNull(filter.attemptAuthentication(request, new MockHttpServletResponse()));
    }

    @Test
    public void successfulAuthentication() throws ServletException, IOException {
        MockHttpServletResponse response = new MockHttpServletResponse();
        JwtUsernameAndPasswordAuthenticationFilter filter =
            new JwtUsernameAndPasswordAuthenticationFilter(authManager, jwtConfig);
        FilterChain filterChain = mock(FilterChain.class);
        Authentication mock = mock(Authentication.class);
        filter.successfulAuthentication(new MockHttpServletRequest(), response, filterChain, mock);
        assertNotNull(response.getHeader(jwtConfig.getHeader()));
    }
}