package user.services;

import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import user.config.UserDtoConfig;
import user.entities.Admin;
import user.entities.Customer;
import user.entities.User;
import user.repositories.UserRepository;

@Service
public class UserService {

    private final transient UserRepository<Customer> customerRepository;
    private final transient UserRepository<Admin> adminRepository;

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Constructor for UserService.
     *
     * @param customerRepository - retrieves Customer Users from database.
     * @param adminRepository    - retrieves Admin Users from database.
     */
    @Autowired
    public UserService(UserRepository customerRepository, UserRepository adminRepository) {
        this.customerRepository = customerRepository;
        this.adminRepository = adminRepository;
    }

    /**
     * Finds User by id.
     *
     * @param userId - long
     * @return Optional of User having this id
     */
    public User getUserById(long userId) {
        return customerRepository.findById(userId);
    }

    /**
     * Finds Customer by userName
     *
     * @param userName - String
     * @return Optional of Customer having this name
     */
    public Optional<Customer> getCustomerByUsername(String userName) {
        return customerRepository.findCustomerByUsername(userName);
    }

    /**
     * Finds Admin by userName
     *
     * @param userName - String
     * @return Optional of Admin having this name
     */
    public Optional<Admin> getAdminByUsername(String userName) {
        return adminRepository.findAdminByUsername(userName);
    }

    /**
     * Register customer.
     *
     * @param data the data
     */
    public void registerCustomer(UserDtoConfig data) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        customerRepository.save(
            new Customer(data.getUsername(), passwordEncoder.encode(data.getPassword()),
                data.isPremiumSubscription()));
    }

    /**
     * Register admin.
     *
     * @param data the data
     */
    public void registerAdmin(UserDtoConfig data) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        adminRepository
            .save(new Admin(data.getUsername(), passwordEncoder.encode(data.getPassword())));
    }

    /**
     * Upgrade a customer to premium.
     *
     * @param customer the customer
     */
    public void upgradeCustomer(Customer customer) {
        customer.setPremiumUser(true);
        customerRepository.save(customer);
    }

    /**
     * Finds customer by username
     *
     * @param username - String
     * @return Optional of Admin having this name
     */
    public Optional<Customer> checkCustomerExists(String username) {
        return customerRepository.findByUsername(username);
    }

    /**
     * Finds Admin by username
     *
     * @param username - String
     * @return Optional of Admin having this name
     */
    public Optional<Admin> checkAdminExists(String username) {
        return adminRepository.findByUsername(username);
    }
}
