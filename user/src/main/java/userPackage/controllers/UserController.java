package userPackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import userPackage.entities.Admin;
import userPackage.entities.Customer;
import userPackage.entities.User;
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

    @GetMapping("/{userName}/getInfo")
    @ResponseBody
    public User getUserInfo(@PathVariable String userName) {
        Customer customer = (Customer) userService.getCustomerByUsername(userName);
        Admin admin = (Admin) userService.getAdminByUsername(userName);

        if (customer == null && admin != null) {
            return admin;
        }
        if (admin == null && customer != null) {
            return customer;
        }
        return null;
    }

}
