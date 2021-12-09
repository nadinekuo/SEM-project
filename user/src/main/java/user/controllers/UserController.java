package user.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("user")
public class UserController {
    private final transient UserService userService;

    @Autowired
    private transient final RestTemplate restTemplate;

    /**
     * Autowired constructor for the class.
     *
     * @param userService userService
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
        this.restTemplate = userService.restTemplate();
    }

    @GetMapping("/{userId}/isPremium")
    @ResponseBody
    public Boolean isUserPremium(@PathVariable Long userId) {
        Customer customer = (Customer) userService.getUserById(userId);
        return customer.isPremiumUser();
    }

    /**
     * Customer registration.
     *
     * @param request the request to register a user.
     * @throws IOException If customer can't be registered
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
     * @param request the request to register an admin
     * @throws IOException if admin can't be registered
     */
    @PostMapping("/registerAdmin/admin")
    public void adminRegistration(HttpServletRequest request) throws IOException {
        UserDtoConfig data =
            new ObjectMapper().readValue(request.getInputStream(), UserDtoConfig.class);
        userService.registerAdmin(data);
    }

}
