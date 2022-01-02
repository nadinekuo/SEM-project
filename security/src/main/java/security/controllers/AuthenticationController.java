package security.controllers;

import com.sun.jersey.core.impl.provider.entity.XMLRootObjectProvider;
import org.bouncycastle.openssl.PasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
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
import java.util.NoSuchElementException;

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

    /**
     * Get the information of a customer
     *
     * @param userName
     * @return AppUser
     */
    public static AppUser getCustomerInfo(@PathVariable String userName){

        String methodSpecificUrl = "/user/" + userName + "/getCustomerInfo";
        List<String> userInfo =
                restTemplate.getForObject(userUrl + methodSpecificUrl, List.class);

        if (userInfo == null) {
            return null;
        }

        return new AppUser(userInfo.get(0), userInfo.get(1), "user");
    }


    /**
     * Get the information of an admin
     *
     * @param userName
     * @return AppUser
     */
    public static AppUser getAdminInfo(@PathVariable String userName){
        String methodSpecificUrl = "/user/" + userName + "/getAdminInfo";
        List<String> userInfo =
                restTemplate.getForObject(userUrl + methodSpecificUrl, List.class);

        if (userInfo == null) {
            return null;
        }

        AppUser user = new AppUser(userInfo.get(0), userInfo.get(1), "admin");
        return user;
    }
}
