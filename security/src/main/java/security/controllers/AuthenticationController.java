package security.controllers;

import org.bouncycastle.openssl.PasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import security.users.AppUser;

import java.util.List;

@RestController
@RequestMapping("authentication")
public class AuthenticationController {

    private static final String userUrl = "http://eureka-user";

    @Autowired
    private static BCryptPasswordEncoder encoder;

    @Autowired
    private static RestTemplate restTemplate;

    /**
     * Autowired constructor for the class
     *
     * @param restTemplate
     */
    public AuthenticationController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public static AppUser getCustomerInfo(@PathVariable String userName){
        String methodSpecificUrl = "/user/" + userName + "/getCustomerInfo";

        List<String> userInfo=
                restTemplate.getForObject(userUrl + methodSpecificUrl, List.class);

        if (userInfo == null) {
            return null;
        }
        AppUser user = new AppUser(userInfo.get(0), userInfo.get(1), "user");
        return user;
    }

    public static AppUser getAdminInfo(@PathVariable String userName){
        String methodSpecificUrl = "/user/" + userName + "/getAdminInfo";

        List<String> userInfo=
                restTemplate.getForObject(userUrl + methodSpecificUrl, List.class);

        if (userInfo == null) {
            return null;
        }
        AppUser user = new AppUser(userInfo.get(0), userInfo.get(1), "admin");
        return user;
    }
}
