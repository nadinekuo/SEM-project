package nl.tudelft.sem.user.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import nl.tudelft.sem.user.config.UserDtoConfig;
import nl.tudelft.sem.user.entities.Admin;
import nl.tudelft.sem.user.entities.Customer;
import nl.tudelft.sem.user.services.UserService;
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

@RestController
@RequestMapping("user")
public class UserController {

    private final transient UserService userService;


    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Instantiates a new User controller.
     *
     * @param userService the user service
     */
    @Autowired
    public UserController(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
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
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get the info of the customer.
     *
     * @param userName the userName
     * @return the response entity
     */
    @GetMapping("/{userName}/getCustomerInfo")
    @ResponseBody
    public ResponseEntity<List<String>> getCustomerInfo(@PathVariable String userName) {
        List<String> customerInfo = new ArrayList<>();
        Optional<Customer> customer = userService.getCustomerByUsername(userName);
        if (customer.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        Customer customerPresent = customer.get();
        customerInfo.add(customerPresent.getUsername());
        customerInfo.add(customerPresent.getPassword());
        return new ResponseEntity<>(customerInfo, HttpStatus.OK);
    }

    /**
     * Get the info of the admin.
     *
     * @param userName the userName
     * @return the response entity
     */
    @GetMapping("/{userName}/getAdminInfo")
    @ResponseBody
    public ResponseEntity<List<String>> getAdminInfo(@PathVariable String userName) {
        List<String> adminInfo = new ArrayList<>();
        Optional<Admin> admin = userService.getAdminByUsername(userName);
        if (admin.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        Admin adminPresent = admin.get();
        adminInfo.add(adminPresent.getUsername());
        adminInfo.add(adminPresent.getPassword());
        return new ResponseEntity<>(adminInfo, HttpStatus.OK);
    }

    /**
     * Customer registration.
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
        try {
            if (userService.checkCustomerExists(data.getUsername())) {
                return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
            }
        } catch (NoSuchElementException e) {
            userService.registerCustomer(data);
        }
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
        try {
            if (userService.checkAdminExists(data.getUsername())) {
                return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
            }
        } catch (NoSuchElementException e) {
            userService.registerAdmin(data);
        }
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
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
