package user.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import user.config.UserDtoConfig;
import user.entities.Customer;
import user.services.UserService;

/**
 * The type User controller.
 */
@RestController
@RequestMapping("user")
public class UserController {

    private final transient UserService userService;

    @Autowired
    private final transient RestTemplate restTemplate;

    /**
     * Instantiates a new User controller.
     *
     * @param userService the user service
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
        this.restTemplate = userService.restTemplate();
    }

    /**
     * Is user premium response entity.
     *
     * @param userId the user id
     * @return the response entity
     */
    @GetMapping("/{userId}/isPremium")
    @ResponseBody
    public ResponseEntity<String> isUserPremium(@PathVariable Long userId) {
        try {
            Customer customer = (Customer) userService.getUserById(userId);
            Boolean isPremium = customer.isPremiumUser();
            return new ResponseEntity<String>(isPremium.toString(), HttpStatus.OK);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            System.out.println("User with id " + userId + " does not exist!!");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Customer registration.
     *
     * @param request the request
     * @throws IOException the io exception
     */
    @PostMapping("/registerCustomer")
    public void customerRegistration(HttpServletRequest request) throws IOException {
        UserDtoConfig data =
            new ObjectMapper().readValue(request.getInputStream(), UserDtoConfig.class);
        userService.registerCustomer(data);
    }

    /**
     * Admin registration.
     *
     * @param request the request
     * @throws IOException the io exception
     */
    @PostMapping("/registerAdmin/admin")
    public void adminRegistration(HttpServletRequest request) throws IOException {
        UserDtoConfig data =
            new ObjectMapper().readValue(request.getInputStream(), UserDtoConfig.class);
        userService.registerAdmin(data);
    }

}
