package nl.tudelft.sem.gateway.filters;

import nl.tudelft.sem.gateway.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;

    public JwtTokenAuthenticationFilter(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    /**
     * Gateway server that checks the JWT token and authenticates the user if it is valid.
     *
     * @param request  the request that needs to be verified
     * @param response the response that is returned
     * @param chain    the chain
     * @throws ServletException servlet exception
     * @throws IOException      IO exception
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        //Gets the header, where the JWT token is
        String header = request.getHeader(jwtConfig.getHeader());

        //Validate the header and checks the prefix
        if (header == null || !header.startsWith(jwtConfig.getPrefix())) {
            chain.doFilter(request, response);        // If not valid, go to the next filter.
            return;
        }

        //Get the token
        String token = header.replace(jwtConfig.getPrefix(), "");

        //Exceptions might be thrown in creating the claims if for example the token is expired
        try {

            //Validate the token
            Claims claims =
                Jwts.parser().setSigningKey(jwtConfig.getSecret().getBytes()).parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            if (username != null) {
                List<String> authorities = (List<String>) claims.get("authorities");

                //Create auth object
                UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(username, null,
                        authorities.stream().map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList()));

                // Authenticate the user
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } catch (Exception e) {
            //In case of failure. Make sure it's clear; so guarantee user won't be authenticated
            SecurityContextHolder.clearContext();
        }

        //Go to the next filter in the filter chain
        chain.doFilter(request, response);
    }

}
