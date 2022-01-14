package nl.tudelft.sem.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.sql.Date;
import java.util.Collections;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class JwtUsernameAndPasswordAuthenticationFilter
    extends UsernamePasswordAuthenticationFilter {

    private final JwtConfig jwtConfig;
    // We use auth manager to validate the user credentials
    private final AuthenticationManager authManager;

    /**
     * Constructor for the filter.
     *
     * @param authManager authentication manager
     * @param jwtConfig   the configuration for JWT we defined
     */
    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authManager,
                                                      JwtConfig jwtConfig) {
        this.authManager = authManager;
        this.jwtConfig = jwtConfig;

        // By default, UsernamePasswordAuthenticationFilter listens to "/login" path.
        // In our case, we use "/eureka-security". So, we need to override the defaults.
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(jwtConfig.getUri()));
    }

    /**
     * Attempt login request.
     *
     * @param request  the request that contains login information
     * @param response the response
     * @return authenticated user
     * @throws AuthenticationException exception
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
        throws AuthenticationException {

        try {

            //Get credentials from request
            UserCredentials creds =
                new ObjectMapper().readValue(request.getInputStream(), UserCredentials.class);

            // Create auth object (contains credentials) which will be used by auth manager
            UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(creds.getUsername(), creds.getPassword(),
                    Collections.emptyList());

            // Authentication manager authenticate the user, and use
            // UserDetailServiceImpl::loadByUsername() method to load the user.
            return authManager.authenticate(authToken);

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Upon successful authentication, generate a token and add it to the header.
     *
     * @param request  the request
     * @param response the response that is returned
     * @param chain    the chain
     * @param auth     the current authenticated user
     * @throws IOException IOException
     * @throws ServletException ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain chain,
                                            Authentication auth)
        throws IOException, ServletException {

        Long now = System.currentTimeMillis();
        String token = Jwts.builder().setSubject(auth.getName())
            // Convert to list of strings.
            // This is important because it affects the way we get them back in the Gateway.
            .claim("authorities", auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())).setIssuedAt(new Date(now))
            .setExpiration(new Date(now + jwtConfig.getExpiration() * 1000))  // in milliseconds
            .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes()).compact();

        // Add token to header
        response.addHeader(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);
    }

    // A class to represent the user credentials
    private static class UserCredentials {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

    }

}
