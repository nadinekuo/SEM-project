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
import org.springframework.web.bind.annotation.PutMapping;
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

    @Autowired
    private ObjectMapper objectMapper;

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

    //    @GetMapping("/{userId}/isPremium")
    //    @ResponseBody
    //    public Boolean isUserPremium(@PathVariable Long userId) {
    //        Customer customer = (Customer) userService.getUserById(userId);
    //        return customer.isPremiumUser();
    //    }

    @GetMapping("/{userId}/isPremium")
    @ResponseBody
    public ResponseEntity<String> isUserPremium(@PathVariable Long userId) {
        try {
            Customer customer = (Customer) userService.getUserById(userId);
            Boolean isPremium = customer.isPremiumUser();
            return new ResponseEntity<>(isPremium.toString(), HttpStatus.OK);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            System.out.println("User with id " + userId + " does not exist!!");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Customer registration.
     *
     * @param request the request to register a user.
     * @throws IOException If customer can't be registered
     */
    @PostMapping("/registerCustomer")
    public ResponseEntity<String> customerRegistration(HttpServletRequest request)
        throws IOException {
        UserDtoConfig data = objectMapper.readValue(request.getInputStream(), UserDtoConfig.class);
        if (data.getUsername() == null || data.getPassword() == null || data.getUsername().isEmpty()
            || data.getPassword().isEmpty()) {
            return new ResponseEntity<>("Fill in all fields.", HttpStatus.BAD_REQUEST);
        }
        if (userService.checkCustomerExists(data.getUsername()).isPresent()) {
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }
        userService.registerCustomer(data);
        return new ResponseEntity<>("User has been registered.", HttpStatus.OK);
    }

    /**
     * Admin registration.
     *
     * @param request the request to register an admin
     * @throws IOException if admin can't be registered
     */
    @PostMapping("/registerAdmin/admin")
    public ResponseEntity<String> adminRegistration(HttpServletRequest request) throws IOException {
        UserDtoConfig data = objectMapper.readValue(request.getInputStream(), UserDtoConfig.class);
        if (data.getUsername() == null || data.getPassword() == null || data.getUsername().isEmpty()
            || data.getPassword().isEmpty()) {
            return new ResponseEntity<>("Fill in all fields.", HttpStatus.BAD_REQUEST);
        }
        if (userService.checkAdminExists(data.getUsername()).isPresent()) {
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }
        userService.registerAdmin(data);
        return new ResponseEntity<>("User has been registered.", HttpStatus.OK);
    }

    @PutMapping("/{userId}/upgrade")
    public ResponseEntity<String> upgradeUser(@PathVariable Long userId) {
        try {
            Customer customer = (Customer) userService.getUserById(userId);
            if (customer.isPremiumUser()) {
                return new ResponseEntity<>("User is already premium!", HttpStatus.BAD_REQUEST);
            }
            userService.upgradeCustomer(customer);
            return new ResponseEntity<>("User has been upgraded to premium.", HttpStatus.OK);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            System.out.println("User with id " + userId + " does not exist!!");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
