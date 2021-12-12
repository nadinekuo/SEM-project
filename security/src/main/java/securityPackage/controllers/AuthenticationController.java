package securityPackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import securityPackage.users.AppUser;

@RestController
@RequestMapping("authentication")
public class AuthenticationController {

    private static final String userUrl = "http://eureka-user";

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

        AppUser user =
                restTemplate.getForObject(userUrl + methodSpecificUrl, AppUser.class);

        return user;
    }

    public static AppUser getAdminInfo(@PathVariable String userName){
        String methodSpecificUrl = "/user/" + userName + "/getAdminInfo";

        AppUser user =
                restTemplate.getForObject(userUrl + methodSpecificUrl, AppUser.class);

        return user;
    }
}
