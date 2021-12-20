package user.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;
import user.config.UserDtoConfig;
import user.entities.Admin;
import user.entities.Customer;
import user.entities.User;
import user.repositories.UserRepository;

@RunWith(MockitoJUnitRunner.class)
class UserServiceTest {

    private final transient Admin admin;

    private final transient Customer customer;

    @Mock
    private transient RestTemplate restTemplate;

    @Mock
    private transient UserRepository<Customer> customerRepository;
    @Mock
    private transient UserRepository<Admin> adminRepository;

    //    @InjectMocks
    private transient UserService userService;

    public UserServiceTest() {
        admin = new Admin("admin", "password");
        customer = new Customer("erwin", "password", true);
    }

    @BeforeEach
    void setUp() {
        customerRepository = Mockito.mock(UserRepository.class);
        adminRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(customerRepository, adminRepository);
    }

    @Test
    public void constructorTest() {
        assertNotNull(userService);
    }

    @Test
    void getUserById() {
        when(customerRepository.findById(1L)).thenReturn(customer);
        User result = userService.getUserById(1L);
        assertEquals(result, customer);
    }

    @Test
    void restTemplate() {
        restTemplate = userService.restTemplate();
        assertNotNull(restTemplate);
    }

    @Test
    void registerCustomer() {
        UserDtoConfig data = new UserDtoConfig("erwin", "password", true);
        userService.registerCustomer(data);
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerRepository).save(customerArgumentCaptor.capture());
        verify(customerRepository, times(1)).save(customer);

        Customer captured = customerArgumentCaptor.getValue();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        assertEquals(captured.getUsername(), "erwin");
        assertTrue(passwordEncoder.matches(data.getPassword(), captured.getPassword()));
        assertEquals(captured.isPremiumUser(), true);
    }

    @Test
    void registerAdmin() {
        UserDtoConfig data = new UserDtoConfig("erwin", "password", true);
        userService.registerAdmin(data);
        ArgumentCaptor<Admin> customerArgumentCaptor = ArgumentCaptor.forClass(Admin.class);

        verify(adminRepository).save(customerArgumentCaptor.capture());
        verify(adminRepository, times(1)).save(admin);

        Admin captured = customerArgumentCaptor.getValue();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        assertEquals(captured.getUsername(), "erwin");
        assertTrue(passwordEncoder.matches(data.getPassword(), captured.getPassword()));
    }

    @Test
    void upgradeCustomer() {
        Customer basic = new Customer("basicuser", "strongpassword", false);
        userService.upgradeCustomer(basic);
        verify(customerRepository, times(1)).save(customer);
        assertTrue(basic.isPremiumUser());
    }

    @Test
    void checkCustomerExists() {
        when(customerRepository.findByUsername("erwin")).thenReturn(Optional.of(customer));
        Optional<Customer> result = userService.checkCustomerExists("erwin");
        verify(customerRepository, times(1)).findByUsername("erwin");
        assertEquals(result.get(), customer);
    }

    @Test
    void checkAdminExists() {
        when(adminRepository.findByUsername("admin")).thenReturn(Optional.of(admin));
        Optional<Admin> result = userService.checkAdminExists("admin");
        verify(adminRepository, times(1)).findByUsername("admin");
        assertEquals(result.get(), admin);
    }
}