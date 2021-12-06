package securityPackage.controllers;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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

    public static User getUserInfo(@PathVariable String userName){
        String methodSpecificUrl = "/user/" + userName + "/getInfo";

        User user =
                restTemplate.getForObject(userUrl + methodSpecificUrl, User.class);

        return user;
    }
}
