package nl.tudelft.sem.user.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;
import nl.tudelft.sem.user.entities.Customer;
import nl.tudelft.sem.user.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    private final transient Customer arslan;

    @Mock
    private transient CustomerRepository customerRepository;

    @Mock
    private transient CustomerService customerService;

    public CustomerServiceTest() {
        arslan = new Customer("arslan123", "password1", true);
    }

    @BeforeEach
    void setup() {
        customerService = new CustomerService(customerRepository);
    }

    @Test
    void getCustomerByIdTest() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(arslan));

        Customer customer = customerService.getCustomerById(1L);

        assertThat(customer).isNotNull();
        assertThat(customer.isPremiumUser()).isTrue();
        assertThat(customer.getPassword()).isEqualTo("password1");

        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void isCustomerPremiumTrueTest() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(arslan));

        assertTrue(customerService.isCustomerPremium(1L));
    }

    @Test
    void isCustomerPremiumFalseTest() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(arslan));
        arslan.setPremiumUser(false);
        assertFalse(customerService.isCustomerPremium(1L));
    }

    @Test
    void getCustomerByIdThrowsExceptionTest() throws Exception {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            customerService.getCustomerById(1L);
        });
    }

    @Test
    void saveCustomerTest() {
        Customer emil = new Customer("emil123", "password2", false);
        when(customerRepository.save(emil)).thenReturn(emil);

        assertThat(customerService.saveCustomer(emil)).isNotNull();
        assertThat(customerService.saveCustomer(emil).getPassword()).isEqualTo("password2");
    }

}