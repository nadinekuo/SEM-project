package user.services;

import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import user.entities.Customer;
import user.repositories.CustomerRepository;

@Service
public class CustomerService {

    @Autowired
    private final CustomerRepository customerRepository;

    /**
     * Instantiates a new Customer service
     *
     * @param customerRepository
     */
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Gets the Customer from the Customer repository.
     *
     * @param CustomerId
     * @return the Customer, else throw IllegalStateException
     */
    public Customer getCustomerById(long CustomerId) {
        return customerRepository.findById(CustomerId).orElseThrow(() -> new NoSuchElementException(
            "Customer with id " + CustomerId + " does not exist!"));
    }

    /**
     * The Customer is persisted into the database using customerRepository
     *
     * @param customer
     * @return the Customer which is saved.
     */
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

}
