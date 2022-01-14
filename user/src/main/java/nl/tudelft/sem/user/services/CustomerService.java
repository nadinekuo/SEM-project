package nl.tudelft.sem.user.services;

import java.util.NoSuchElementException;
import nl.tudelft.sem.user.entities.Customer;
import nl.tudelft.sem.user.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private final CustomerRepository customerRepository;

    /**
     * Instantiates a new Customer service.
     *
     * @param customerRepository customer repository
     */
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Gets the Customer from the Customer repository.
     *
     * @param customerId customer id
     * @return the Customer, else throw IllegalStateException
     */
    public Customer getCustomerById(long customerId) {
        return customerRepository.findById(customerId).orElseThrow(() -> new NoSuchElementException(
            "Customer with id " + customerId + " does not exist!"));
    }

    public Boolean isCustomerPremium(long customerId) {
        return getCustomerById(customerId).isPremiumUser();
    }

    /**
     * The Customer is persisted into the database using customerRepository.
     *
     * @param customer customer
     * @return the Customer which is saved.
     */
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

}
