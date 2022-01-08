package security.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
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
        ResponseEntity<List> response;
        try {
            response =
                    restTemplate.getForEntity(userUrl + methodSpecificUrl, List.class);
        } catch (HttpClientErrorException e) {
            return null;
        }
        List<String> userInfo = response.getBody();

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
        ResponseEntity<List> response;
        try {
            response =
                restTemplate.getForEntity(userUrl + methodSpecificUrl, List.class);
        } catch (HttpClientErrorException e) {
            return null;
        }
        List<String> userInfo = response.getBody();

        return new AppUser(userInfo.get(0), userInfo.get(1), "admin");

    }
}
