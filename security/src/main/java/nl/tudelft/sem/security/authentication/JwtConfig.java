package nl.tudelft.sem.security.authentication;

import org.springframework.beans.factory.annotation.Value;

public class JwtConfig {
    @Value("${security.jwt.uri:/eureka-security/**}")
    private String uri;

    @Value("${security.jwt.header:Authorization}")
    private String header;

    @Value("${security.jwt.prefix:Bearer }")
    private String prefix;

    //Expires in a day
    @Value("${security.jwt.expiration:#{24*60*60}}")
    private int expiration;

    @Value("${security.jwt.secret:JwtSecretKey}")
    private String secret;

    public String getUri() {
        return uri;
    }

    //Getters
    public String getHeader() {
        return header;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getExpiration() {
        return expiration;
    }

    public String getSecret() {
        return secret;
    }
}
