package user.controllers;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.NoSuchElementException;
import javax.servlet.ServletInputStream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import user.config.UserDtoConfig;
import user.entities.Admin;
import user.entities.Customer;
import user.services.UserService;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    private final transient long userId = 1L;

    private final transient String userName = "emma";

    @Mock
    transient UserService userService;

    @Mock
    transient ObjectMapper objectMapper;

    @Autowired
    private transient MockMvc mockMvc;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup() {
        this.mockMvc =
            MockMvcBuilders.standaloneSetup(new UserController(userService, objectMapper)).build();

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
        when(userService.getCustomerByUsername(userName)).thenReturn(of(customer));
        MvcResult result = mockMvc.perform(get("/user/{userName}/getCustomerInfo", userName))
            .andExpect(status().isOk()).andReturn();
        verify(userService).getCustomerByUsername(userName);
        assertThat(result.getResponse().getContentType()).isNotNull();
    }

    @Test
    void getAdminInfoTest() throws Exception {
        Admin admin = new Admin();
        when(userService.getAdminByUsername(userName)).thenReturn(of(admin));
        MvcResult result = mockMvc.perform(get("/user/{userName}/getAdminInfo", userName))
            .andExpect(status().isOk()).andReturn();
        verify(userService).getAdminByUsername(userName);
        assertThat(result.getResponse().getContentType()).isNotNull();
    }

    @Test
    void getFalseCustomerInfoTest() throws Exception {
        when(userService.getCustomerByUsername(userName)).thenReturn(empty());
        MvcResult result = mockMvc.perform(get("/user/{userName}/getCustomerInfo", userName))
            .andExpect(status().isBadRequest()).andReturn();
        verify(userService).getCustomerByUsername(userName);
        assertThat(result.getResponse().getContentType()).isNull();
    }

    @Test
    void getFalseAdminInfoTest() throws Exception {
        when(userService.getAdminByUsername(userName)).thenReturn(empty());
        MvcResult result = mockMvc.perform(get("/user/{userName}/getAdminInfo", userName))
            .andExpect(status().isBadRequest()).andReturn();
        verify(userService).getAdminByUsername(userName);
        assertThat(result.getResponse().getContentType()).isNull();
    }

    @Test
    void customerRegistrationValidTest() throws Exception {
        UserDtoConfig data = new UserDtoConfig("customer", "password", true);
        when(objectMapper.readValue(any(ServletInputStream.class),
            eq(UserDtoConfig.class))).thenReturn(data);
        when(userService.checkCustomerExists("customer")).thenThrow(NoSuchElementException.class);
        MvcResult result =
            mockMvc.perform(post("/user/registerCustomer")).andExpect(status().isOk()).andReturn();
        Assertions.assertThat(result.getResponse().getContentAsString())
            .isEqualTo("User has been registered.");
        verify(userService).registerCustomer(data);
    }

    @Test
    void customerRegistrationNameTakenTest() throws Exception {
        UserDtoConfig data = new UserDtoConfig("customer", "password", true);
        when(objectMapper.readValue(any(ServletInputStream.class),
            eq(UserDtoConfig.class))).thenReturn(data);
        when(userService.checkCustomerExists("customer")).thenReturn(true);
        MvcResult result =
            mockMvc.perform(post("/user/registerCustomer")).andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertThat(result.getResponse().getContentAsString())
            .isEqualTo("Username is already taken!");
        verify(userService, never()).registerCustomer(data);

    }

    @Test
    void customerRegistrationNameNullTest() throws Exception {
        UserDtoConfig data = new UserDtoConfig(null, "password", true);
        when(objectMapper.readValue(any(ServletInputStream.class),
            eq(UserDtoConfig.class))).thenReturn(data);
        MvcResult result =
            mockMvc.perform(post("/user/registerCustomer")).andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertThat(result.getResponse().getContentAsString())
            .isEqualTo("Fill in all fields.");
        verify(userService, never()).registerCustomer(data);
    }

    @Test
    void customerRegistrationPasswordNullTest() throws Exception {
        UserDtoConfig data = new UserDtoConfig("customer", null, true);
        when(objectMapper.readValue(any(ServletInputStream.class),
            eq(UserDtoConfig.class))).thenReturn(data);
        MvcResult result =
            mockMvc.perform(post("/user/registerCustomer")).andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertThat(result.getResponse().getContentAsString())
            .isEqualTo("Fill in all fields.");
        verify(userService, never()).registerCustomer(data);
    }

    @Test
    void customerRegistrationNameEmptyTest() throws Exception {
        UserDtoConfig data = new UserDtoConfig("", "password", true);
        when(objectMapper.readValue(any(ServletInputStream.class),
            eq(UserDtoConfig.class))).thenReturn(data);
        MvcResult result =
            mockMvc.perform(post("/user/registerCustomer")).andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertThat(result.getResponse().getContentAsString())
            .isEqualTo("Fill in all fields.");
        verify(userService, never()).registerCustomer(data);
    }

    @Test
    void customerRegistrationPasswordEmptyTest() throws Exception {
        UserDtoConfig data = new UserDtoConfig("customer", "", true);
        when(objectMapper.readValue(any(ServletInputStream.class),
            eq(UserDtoConfig.class))).thenReturn(data);
        MvcResult result =
            mockMvc.perform(post("/user/registerCustomer")).andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertThat(result.getResponse().getContentAsString())
            .isEqualTo("Fill in all fields.");
        verify(userService, never()).registerCustomer(data);
    }

    @Test
    void adminRegistrationValidTest() throws Exception {
        UserDtoConfig data = new UserDtoConfig("admin", "password", true);
        when(objectMapper.readValue(any(ServletInputStream.class),
            eq(UserDtoConfig.class))).thenReturn(data);
        when(userService.checkAdminExists("admin")).thenThrow(NoSuchElementException.class);
        MvcResult result =
            mockMvc.perform(post("/user/registerAdmin/admin")).andExpect(status().isOk())
                .andReturn();
        Assertions.assertThat(result.getResponse().getContentAsString())
            .isEqualTo("User has been registered.");
        verify(userService).registerAdmin(data);
    }

    @Test
    void adminRegistrationNameTakenTest() throws Exception {
        UserDtoConfig data = new UserDtoConfig("admin", "password", true);
        when(objectMapper.readValue(any(ServletInputStream.class),
            eq(UserDtoConfig.class))).thenReturn(data);
        when(userService.checkAdminExists("admin")).thenReturn(true);
        MvcResult result =
            mockMvc.perform(post("/user/registerAdmin/admin")).andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertThat(result.getResponse().getContentAsString())
            .isEqualTo("Username is already taken!");
        verify(userService, never()).registerCustomer(data);
    }

    @Test
    void adminRegistrationNameNullTest() throws Exception {
        UserDtoConfig data = new UserDtoConfig(null, "password", true);
        when(objectMapper.readValue(any(ServletInputStream.class),
            eq(UserDtoConfig.class))).thenReturn(data);
        MvcResult result =
            mockMvc.perform(post("/user/registerAdmin/admin")).andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertThat(result.getResponse().getContentAsString())
            .isEqualTo("Fill in all fields.");
        verify(userService, never()).registerCustomer(data);
    }

    @Test
    void adminRegistrationPasswordNullTest() throws Exception {
        UserDtoConfig data = new UserDtoConfig("admin", null, true);
        when(objectMapper.readValue(any(ServletInputStream.class),
            eq(UserDtoConfig.class))).thenReturn(data);
        MvcResult result =
            mockMvc.perform(post("/user/registerAdmin/admin")).andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertThat(result.getResponse().getContentAsString())
            .isEqualTo("Fill in all fields.");
        verify(userService, never()).registerCustomer(data);
    }

    @Test
    void adminRegistrationNameEmptyTest() throws Exception {
        UserDtoConfig data = new UserDtoConfig("", "password", true);
        when(objectMapper.readValue(any(ServletInputStream.class),
            eq(UserDtoConfig.class))).thenReturn(data);
        MvcResult result =
            mockMvc.perform(post("/user/registerAdmin/admin")).andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertThat(result.getResponse().getContentAsString())
            .isEqualTo("Fill in all fields.");
        verify(userService, never()).registerCustomer(data);
    }

    @Test
    void adminRegistrationPasswordEmptyTest() throws Exception {
        UserDtoConfig data = new UserDtoConfig("admin", "", true);
        when(objectMapper.readValue(any(ServletInputStream.class),
            eq(UserDtoConfig.class))).thenReturn(data);
        MvcResult result =
            mockMvc.perform(post("/user/registerAdmin/admin")).andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertThat(result.getResponse().getContentAsString())
            .isEqualTo("Fill in all fields.");
        verify(userService, never()).registerCustomer(data);
    }
}
