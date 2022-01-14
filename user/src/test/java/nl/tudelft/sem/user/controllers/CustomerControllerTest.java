package nl.tudelft.sem.user.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.NoSuchElementException;
import nl.tudelft.sem.user.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import nl.tudelft.sem.user.services.CustomerService;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class CustomerControllerTest {

    private final transient long customerId = 1L;
    private final transient String userName = "emma";
    private final transient Customer customer = new Customer("Panagiotis", "pass", true);
    @Mock
    transient CustomerService customerService;
    @Autowired
    private transient MockMvc mockMvc;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup() {
        this.mockMvc =
            MockMvcBuilders.standaloneSetup(new CustomerController(customerService)).build();
    }

    @Test
    void getCustomerByIdTest() throws Exception {
        when(customerService.getCustomerById(customerId)).thenReturn(customer);
        mockMvc.perform(get("/customer/{customerId}", customerId)).andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getCustomerByIdThrowsExceptionTest() throws Exception {
        when(customerService.getCustomerById(customerId)).thenThrow(NoSuchElementException.class);
        mockMvc.perform(get("/customer/{customerId}", customerId))
            .andExpect(status().isBadRequest()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    void isUserPremiumTest() throws Exception {
        when(customerService.isCustomerPremium(customerId)).thenReturn(true);
        mockMvc.perform(get("/customer/{customerId}/isPremiumUser", customerId))
            .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    void isUserPremiumThrowsExceptionTest() throws Exception {
        when(customerService.isCustomerPremium(1L)).thenThrow(NoSuchElementException.class);
        mockMvc.perform(get("/customer/{customerId}/isPremiumUser", customerId))
            .andExpect(status().isBadRequest()).andDo(MockMvcResultHandlers.print());
    }

}
