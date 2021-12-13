package userPackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import user.entities.Admin;
import user.entities.Customer;
import user.entities.User;
import userPackage.services.UserService;

@RestController
@RequestMapping("user")
public class UserController {
    private final transient UserService userService;

    @Autowired
    private final RestTemplate restTemplate;


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

    @GetMapping("/{userName}/getCustomerInfo")
    @ResponseBody
    public User getCustomerInfo(@PathVariable String userName) {
        Customer customer = userService.getCustomerByUsername(userName);
        return customer;
    }

    @GetMapping("/{userName}/getAdminInfo")
    @ResponseBody
    public User getAdminInfo(@PathVariable String userName) {
        Admin admin = userService.getAdminByUsername(userName);
        return admin;
    }

}
