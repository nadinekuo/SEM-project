package user.controllers;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import user.entities.Customer;
import user.services.UserService;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    private final transient long userId = 1L;

    @Mock
    transient UserService userService;

    @Mock
    transient ObjectMapper objectMapper;

    @Autowired
    private transient MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService)).build();

    }

    @Test
    void isUserPremiumValidTest() throws Exception {
        Customer customer = new Customer();
        customer.setPremiumUser(true);
        when(userService.getUserById(userId)).thenReturn(customer);
        mockMvc.perform(get("/user/{userId}/isPremium", userId)).andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print());
        verify(userService).getUserById(userId);
    }

    @Test
    void isUserPremiumNotValidTest() throws Exception {
        when(userService.getUserById(1L)).thenThrow(NoSuchElementException.class);
        mockMvc.perform(get("/user/{userId}/isPremium", userId)).andExpect(status().isBadRequest())
            .andDo(MockMvcResultHandlers.print());
        verify(userService).getUserById(userId);
    }

    @Test
    void upgradeBasicUserValidTest() throws Exception {
        Customer customer = new Customer();
        when(userService.getUserById(userId)).thenReturn(customer);
        mockMvc.perform(put("/user/{userId}/upgrade", userId)).andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print());
        verify(userService).getUserById(userId);
        verify(userService).upgradeCustomer(customer);
    }

    @Test
    void upgradePremiumUserTest() throws Exception {
        Customer customer = new Customer();
        customer.setPremiumUser(true);
        when(userService.getUserById(userId)).thenReturn(customer);
        mockMvc.perform(put("/user/{userId}/upgrade", userId)).andExpect(status().isBadRequest())
            .andDo(MockMvcResultHandlers.print());
        verify(userService).getUserById(userId);
        verify(userService, never()).upgradeCustomer(customer);
    }

    @Test
    void upgradeUserNotValidTest() throws Exception {
        Customer customer = new Customer();
        when(userService.getUserById(userId)).thenThrow(NoSuchElementException.class);
        mockMvc.perform(put("/user/{userId}/upgrade", userId)).andExpect(status().isBadRequest())
            .andDo(MockMvcResultHandlers.print());
        verify(userService, never()).upgradeCustomer(customer);
    }

    @Test
    void getCustomerInfoTest() throws Exception {
        Customer customer = new Customer();
        when(userService.getCustomerByUsername(userName)).thenReturn(Optional.of(customer));
        MvcResult result = mockMvc.perform(get("/user/{userName}/getCustomerInfo", userName))
            .andExpect(status().isOk()).andReturn();
        verify(userService).getCustomerByUsername(userName);
        assertThat(result.getResponse().getContentType()).isNotNull();
    }

    @Test
    void getAdminInfoTest() throws Exception {
        Admin admin = new Admin();
        when(userService.getAdminByUsername(userName)).thenReturn(Optional.of(admin));
        MvcResult result = mockMvc.perform(get("/user/{userName}/getAdminInfo", userName))
            .andExpect(status().isOk()).andReturn();
        verify(userService).getAdminByUsername(userName);
        assertThat(result.getResponse().getContentType()).isNotNull();
    }

    @Test
    void getFalseCustomerInfoTest() throws Exception {
        when(userService.getCustomerByUsername(userName)).thenReturn(Optional.empty());
        MvcResult result = mockMvc.perform(get("/user/{userName}/getCustomerInfo", userName))
            .andExpect(status().isBadRequest()).andReturn();
        verify(userService).getCustomerByUsername(userName);
        assertThat(result.getResponse().getContentType()).isNull();
    }

    @Test
    void getFalseAdminInfoTest() throws Exception {
        when(userService.getAdminByUsername(userName)).thenReturn(Optional.empty());
        MvcResult result = mockMvc.perform(get("/user/{userName}/getAdminInfo", userName))
            .andExpect(status().isBadRequest()).andReturn();
        verify(userService).getAdminByUsername(userName);
        assertThat(result.getResponse().getContentType()).isNull();
    }

    //    @Test
    //    void customerRegistrationValidTest() throws Exception {
    //        UserDtoConfig data = new UserDtoConfig("customer", "password", true);
    //        Customer customer = new Customer("customer", "password", true);
    //        when(objectMapper.readValue(any(ServletInputStream.class), eq(UserDtoConfig.class)))
    //            .thenReturn(data);
    //        when(userService.getCustomerByUsername("erwin")).thenReturn(Optional.of(customer));
    //        mockMvc.perform(post("/user/registerCustomer")).andExpect(status().isOk())
    //            .andDo(MockMvcResultHandlers.print());
    //        verify(userService).registerCustomer(data);
    //    }

}