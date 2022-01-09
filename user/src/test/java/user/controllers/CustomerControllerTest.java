package user.controllers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.NoSuchElementException;
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
import user.services.CustomerService;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class CustomerControllerTest {

    private final transient long customerId = 1L;

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
    public void getCustomerByIdTest() throws Exception {
        mockMvc.perform(get("/customer/{id}", customerId)).andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print());

        verify(customerService).getCustomerById(customerId);
    }

    @Test
    public void getCustomerByIdThrowsExceptionTest() throws Exception {
        when(customerService.getCustomerById(customerId)).thenThrow(NoSuchElementException.class);
        mockMvc.perform(get("/customer/{id}", customerId)).andExpect(status().isBadRequest())
            .andDo(MockMvcResultHandlers.print());
        verify(customerService).getCustomerById(customerId);
    }

}
