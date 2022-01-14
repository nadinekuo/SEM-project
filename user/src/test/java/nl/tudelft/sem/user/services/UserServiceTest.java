package nl.tudelft.sem.user.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;
import nl.tudelft.sem.user.config.UserDtoConfig;
import nl.tudelft.sem.user.entities.Admin;
import nl.tudelft.sem.user.entities.Customer;
import nl.tudelft.sem.user.entities.User;
import nl.tudelft.sem.user.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

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
    void getUserByIdTest() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        User result = userService.getUserById(1L);
        assertEquals(result, customer);
    }

    @Test
    void getUserByIdThrowsException() throws Exception {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            userService.getUserById(1L);
        });
    }

    @Test
    void restTemplateTest() {
        restTemplate = userService.restTemplate();
        assertNotNull(restTemplate);
    }

    @Test
    void registerCustomerTest() {
        UserDtoConfig data = new UserDtoConfig("erwin", "password", true);
        userService.registerCustomer(data);
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerRepository).save(customerArgumentCaptor.capture());
        verify(customerRepository, times(1)).save(customer);

        Customer captured = customerArgumentCaptor.getValue();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        assertEquals(captured.getUsername(), "erwin");
        assertTrue(passwordEncoder.matches(data.getPassword(), captured.getPassword()));
        assertTrue(captured.isPremiumUser());
    }

    @Test
    void registerAdminTest() {
        UserDtoConfig data = new UserDtoConfig("erwin", "password", true);
        userService.registerAdmin(data);
        ArgumentCaptor<Admin> customerArgumentCaptor = ArgumentCaptor.forClass(Admin.class);

        verify(adminRepository).save(customerArgumentCaptor.capture());
        verify(adminRepository, times(1)).save(admin);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Admin captured = customerArgumentCaptor.getValue();

        assertEquals(captured.getUsername(), "erwin");
        assertTrue(passwordEncoder.matches(data.getPassword(), captured.getPassword()));
    }

    @Test
    void upgradeCustomerTest() {
        UserDtoConfig basicCustomer = new UserDtoConfig("erwin", "password", false);
        userService.registerCustomer(basicCustomer);
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerRepository).save(customerArgumentCaptor.capture());
        verify(customerRepository, times(1)).save(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertFalse(capturedCustomer.isPremiumUser());

        when(customerRepository.findById(0)).thenReturn(Optional.of(capturedCustomer));

        userService.upgradeCustomer(capturedCustomer);
        assertTrue(capturedCustomer.isPremiumUser());
    }

    @Test
    void getCustomerByUsernameTest() {
        when(customerRepository.findCustomerByUsername("erwin")).thenReturn(Optional.of(customer));
        Optional<Customer> result = userService.getCustomerByUsername("erwin");
        verify(customerRepository, times(1)).findCustomerByUsername("erwin");
        assertEquals(result.get(), customer);
    }

    @Test
    void checkCustomerExistsTest() {
        when(customerRepository.findByUsername("erwin")).thenReturn(Optional.of(customer));
        boolean result = userService.checkCustomerExists("erwin");
        verify(customerRepository, times(1)).findByUsername("erwin");
        assertTrue(result);
    }

    @Test
    void checkCustomerExistsThrowsExceptionTest() {
        when(customerRepository.findByUsername("erwin")).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> {
            userService.checkCustomerExists("erwin");
        });
    }

    @Test
    void getAdminByUsernameTest() {
        when(adminRepository.findAdminByUsername("admin")).thenReturn(Optional.of(admin));
        Optional<Admin> result = userService.getAdminByUsername("admin");
        verify(adminRepository, times(1)).findAdminByUsername("admin");
        assertEquals(result.get(), admin);
    }

    @Test
    void checkAdminExistsTest() {
        when(adminRepository.findByUsername("admin")).thenReturn(Optional.of(admin));
        boolean result = userService.checkAdminExists("admin");
        verify(adminRepository, times(1)).findByUsername("admin");
        assertTrue(result);
    }

    @Test
    void checkAdminExistsThrowsExceptionTest() {
        when(adminRepository.findByUsername("admin")).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> {
            userService.checkAdminExists("admin");
        });
    }

    @Test
    void getCustomerByUsernameFalseTest() {
        Optional<Customer> result = userService.getCustomerByUsername("emma");
        verify(customerRepository, times(1)).findCustomerByUsername("emma");
        assertEquals(Optional.empty(), result);
    }

    @Test
    void getAdminByUsernameFalseTest() {
        Optional<Admin> result = userService.getAdminByUsername("adminFalse");
        verify(adminRepository, times(1)).findAdminByUsername("adminFalse");
        assertEquals(Optional.empty(), result);
    }
}
