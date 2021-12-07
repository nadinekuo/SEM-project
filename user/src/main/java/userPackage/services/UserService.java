package userPackage.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import userPackage.entities.Admin;
import userPackage.entities.Customer;
import userPackage.entities.User;
import userPackage.repositories.UserRepository;

@Service
public class UserService {

    private final transient UserRepository<Customer> customerRepository;
    private final transient UserRepository<Admin> adminRepository;

    /**
     * Constructor for UserService.
     *
     * @param customerRepository - retrieves Customer Users from database.
     * @param adminRepository - retrieves Admin Users from database.
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
    public Customer getCustomerByUsername(String userName) {
        return customerRepository.findByUsername(userName);
    }

    /**
     * Finds Admin by userName
     *
     * @param userName - String
     * @return Optional of Admin having this name
     */
    public Admin getAdminByUsername(String userName) {
        return adminRepository.findByUsername(userName);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
