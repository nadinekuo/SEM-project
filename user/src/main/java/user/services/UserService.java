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

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Finds User by id.
     *
     * @param userId - long
     * @return Optional of User having this id
     * @throws NoSuchElementException NoSuchElementException
     */
    public User getUserById(long userId) {
        return customerRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException("user with id " + userId + "does not exist!"));
    }

    /**
     * Finds Customer by userName.
     *
     * @param userName - String
     * @return Optional of Customer having this name
     */
    public Optional<Customer> getCustomerByUsername(String userName) {
        return customerRepository.findCustomerByUsername(userName);
    }

    /**
     * Finds Admin by userName.
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
        customerRepository
                .save(new Customer(data.getUsername(), passwordEncoder.encode(data.getPassword()),
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
        long id = customer.getId();
        customerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Customer does not exist"));

        customer.setPremiumUser(true);
        customerRepository.save(customer);
    }

    /**
     * Check if the Customer exists through the database.
     *
     * @param username user name
     * @return true if customer exists, else false
     * @throws NoSuchElementException NoSuchElementException
     */
    public boolean checkCustomerExists(String username) {
        Customer customer = customerRepository.findByUsername(username).orElseThrow(
                () -> new NoSuchElementException("User with username "
                        + username + " does not exist"));
        return true;
    }

    /**
     * Check if the admin exists through the database.
     *
     * @param username user name
     * @return true if admin exists, else false
     * @throws NoSuchElementException NoSuchElementException
     */
    public boolean checkAdminExists(String username) {
        Admin admin = adminRepository.findByUsername(username).orElseThrow(
                () -> new NoSuchElementException("Admin with username "
                        + username + " does not exist"));
        return true;
    }
}
