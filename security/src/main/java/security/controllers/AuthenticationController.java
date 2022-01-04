package security.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import security.users.AppUser;

@RestController
@RequestMapping("authentication")
public class AuthenticationController {

    private static final String userUrl = "http://eureka-user";

    @Autowired
    private static RestTemplate restTemplate;

    /**
     * Constructor for the class.
     *
     * @param restTemplate restTemplate
     */
    public AuthenticationController(RestTemplate restTemplate) {
        AuthenticationController.restTemplate = restTemplate;
    }

    /**
     * Get the information of a customer.
     *
     * @param userName username
     * @return AppUser class that represents user entity
     */
    public static AppUser getCustomerInfo(@PathVariable String userName) {

        String methodSpecificUrl = "/user/" + userName + "/getCustomerInfo";
        List<String> userInfo = restTemplate.getForObject(userUrl + methodSpecificUrl, List.class);

        if (userInfo == null) {
            return null;
        }

        return new AppUser(userInfo.get(0), userInfo.get(1), "user");
    }

    /**
     * Get the information of an admin.
     *
     * @param userName username
     * @return AppUser representation for the user class
     */
    public static AppUser getAdminInfo(@PathVariable String userName) {
        String methodSpecificUrl = "/user/" + userName + "/getAdminInfo";
        List<String> userInfo = restTemplate.getForObject(userUrl + methodSpecificUrl, List.class);

        if (userInfo == null) {
            return null;
        }

        AppUser user = new AppUser(userInfo.get(0), userInfo.get(1), "admin");
        return user;
    }
}
