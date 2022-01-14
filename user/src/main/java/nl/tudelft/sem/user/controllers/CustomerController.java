package nl.tudelft.sem.user.controllers;

import java.util.NoSuchElementException;
import nl.tudelft.sem.user.entities.Customer;
import nl.tudelft.sem.user.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("customer")
public class CustomerController {

    @Autowired
    private final CustomerService customerService;

    /**
     * Instantiates a new Customer Controller.
     *
     * @param customerService the customer service
     */
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Gets the Customer.
     *
     * @param id Long id
     * @return the customer
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable long id) {
        try {
            Customer customer = customerService.getCustomerById(id);
            return new ResponseEntity<>(customer, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Gets if customer is premium.
     *
     * @param userId the user id
     * @return if the customer is premium
     */
    @GetMapping("/{userId}/isPremiumUser")
    public ResponseEntity<String> getCustomerIsPremium(@PathVariable long userId) {
        try {
            Boolean res = customerService.isCustomerPremium(userId);
            return new ResponseEntity<>(res.toString(), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
