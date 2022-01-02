package user.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import user.config.UserDtoConfig;
import user.entities.Customer;
import user.services.UserService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * The type User controller.
 */
@RestController
@RequestMapping("user")
public class UserController {

    private final transient UserService userService;

    //TODO if the restTemplate isn't used we should remove it
    @Autowired
    private final transient RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

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
            return new ResponseEntity<>(isPremium.toString(), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Customer registration response entity.
     *
     * @param request the request
     * @return the response entity
     * @throws IOException the io exception
     */
    @PostMapping("/registerCustomer")
    public ResponseEntity<String> customerRegistration(HttpServletRequest request)
        throws IOException {
        UserDtoConfig data = objectMapper.readValue(request.getInputStream(), UserDtoConfig.class);
        if (data.getUsername() == null || data.getPassword() == null || data.getUsername().isEmpty()
            || data.getPassword().isEmpty()) {
            return new ResponseEntity<>("Fill in all fields.", HttpStatus.BAD_REQUEST);
        }
        if (userService.checkCustomerExists(data.getUsername())) {
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }
        userService.registerCustomer(data);
        return new ResponseEntity<>("User has been registered.", HttpStatus.OK);
    }

    /**
     * Admin registration response entity.
     *
     * @param request the request
     * @return the response entity
     * @throws IOException the io exception
     */
    @PostMapping("/registerAdmin/admin")
    public ResponseEntity<String> adminRegistration(HttpServletRequest request) throws IOException {
        UserDtoConfig data = objectMapper.readValue(request.getInputStream(), UserDtoConfig.class);
        if (data.getUsername() == null || data.getPassword() == null || data.getUsername().isEmpty()
            || data.getPassword().isEmpty()) {
            return new ResponseEntity<>("Fill in all fields.", HttpStatus.BAD_REQUEST);
        }
        if (userService.checkAdminExists(data.getUsername())) {
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }
        userService.registerAdmin(data);
        return new ResponseEntity<>("User has been registered.", HttpStatus.OK);
    }

    /**
     * Turns basic subscription of customers into premium.
     *
     * @param userId - long
     * @return ResponseEntity containing error message, if applicable.
     */
    @PutMapping("/{userId}/upgrade")
    public ResponseEntity<String> upgradeUser(@PathVariable Long userId) {
        try {
            Customer customer = (Customer) userService.getUserById(userId);
            if (customer.isPremiumUser()) {
                return new ResponseEntity<>("User is already premium!", HttpStatus.BAD_REQUEST);
            }
            userService.upgradeCustomer(customer);
            return new ResponseEntity<>("User has been upgraded to premium.", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
