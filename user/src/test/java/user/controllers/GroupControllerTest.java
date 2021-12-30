package user.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import user.entities.Customer;
import user.entities.Group;
import user.services.GroupService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class GroupControllerTest {

    private final transient long groupId = 15L;

    @Mock
    transient GroupService groupService;

    @Mock
    transient RestTemplate restTemplate;
    @Autowired
    private transient MockMvc mockMvc;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup() {

        when(groupService.restTemplate()).thenReturn(restTemplate);
        this.mockMvc = MockMvcBuilders.standaloneSetup(new GroupController(groupService)).build();
    }

    @Test
    public void getGroupSizeTest() throws Exception {
        mockMvc.perform(get("/group/{groupId}/getGroupSize", groupId)).andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print());
        verify(groupService).getGroupSizeById(groupId);
    }

    @Test
    public void getGroupSizeInvalidTest() throws Exception {
        when(groupService.getGroupSizeById(groupId)).thenThrow(new IllegalStateException());
        mockMvc.perform(get("/group/{groupId}/getGroupSize", groupId))
            .andExpect(status().isBadRequest()).andDo(MockMvcResultHandlers.print());
        verify(groupService).getGroupSizeById(groupId);
    }

    @Test
    void getGroupById() throws Exception {
        mockMvc.perform(get("/group/{id}/", groupId)).andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print());
        verify(groupService).getGroupById(groupId);
    }

    @Test
    void getGroupByGroupName() throws Exception {
        mockMvc.perform(get("/group/groupName/{groupName}/", "basketball"))
            .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(groupService).getGroupByGroupName("basketball");
    }

    @Test
    void getUsersInGroup() throws Exception {
        mockMvc.perform(get("/group/getCustomers/{id}/", groupId)).andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print());
        verify(groupService).getUsersInaGroup(groupId);
    }

    @Test
    void createGroupValid() throws Exception {
        when(groupService.createGroup("basketball")).thenReturn(true);
        mockMvc.perform(post("/group/create/{groupName}/", "basketball")).andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void createGroupInvalid() throws Exception {
        when(groupService.createGroup("basketball")).thenThrow(IllegalStateException.class);
        mockMvc.perform(post("/group/create/{groupName}/", "basketball"))
            .andExpect(status().isBadRequest()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    void addCustomerToGroup() throws Exception {
        mockMvc.perform(put("/group/addCustomer/{groupId}/{customerId}", groupId, 1L))
            .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(groupService).addCustomerToGroup(1L, groupId);
    }

    @Test
    void makeValidGroupReservation() throws Exception {
        final String url =
            "http://eureka-reservation/reservation/" + 0L + "/" + groupId + "/"
                + "2099-01-06T21:00:00" + "/" + 2 + "/" + false + "/" + "makeSportRoomBooking";
        List<Customer> customers = List.of(new Customer("arslan123", "password1", false),
            new Customer("emil123", "password2", false),
            new Customer("emma123", "password3", false));

        Group group = new Group("basketball", customers);
        group.setGroupId(groupId);
        when(groupService.getUsersInaGroup(groupId)).thenReturn(customers);
        mockMvc.perform(
                post("/group/reservation/{groupId}/{sportRoomId}/{date}"
                        + "/makeSportRoomBooking",
                    groupId,
                    2L, "2099-01-06T21:00:00")).andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print());

        verify(restTemplate, times(customers.size())).exchange(eq(url), eq(HttpMethod.POST),
            any(HttpEntity.class), eq(String.class));

    }
}
