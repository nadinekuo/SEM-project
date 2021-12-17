package user.services;

import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import user.entities.Customer;
import user.entities.Group;
import user.repositories.GroupRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GroupServiceTest {

    @Mock
    private transient GroupRepository groupRepository;

    @Mock
    private transient CustomerService customerService;

    private transient GroupService groupService;

    private final transient Customer arslan;
    private final transient Customer emil;
    private final transient Customer emma;
    private final transient Customer erwin;
    private final transient Customer nadine;
    private final transient Customer panagiotis;
    private final transient Group group1;
    private final transient Group group2;
    private transient Group group3;

    /**
     * Constructor for test.
     */
    public GroupServiceTest() {
        arslan = new Customer("arslan123", "password1", true);
        emil = new Customer("emil123", "password2", false);
        emma = new Customer("emma123", "password3", true);
        erwin = new Customer("erwin123", "password4", false);
        nadine = new Customer("nadine123", "password5", true);
        panagiotis = new Customer("panas123", "password6", false);

        group1 = new Group(33L, "soccerTeam1", List.of(arslan, emil, nadine, erwin, emma, panagiotis));
        group2 = new Group(42L, "volleyballTeam3", List.of(emma, panagiotis, erwin));
    }

    /**
     * runs before each test.
     */
    @BeforeEach
    void setup() {
        groupService = new GroupService(groupRepository, customerService);
    }

    @Test
    public void testConstructor() {
        assertNotNull(groupService);
    }

    @Test
    public void restTemplateTest() {
        RestTemplate restTemplate = groupService.restTemplate();
        assertNotNull(restTemplate);
    }

    @Test
    public void getGroupByIdTest() {
        when(groupRepository.findByGroupId(33L)).thenReturn(Optional.of(group1));

        Group result = groupService.getGroupById(33L);

        assertThat(result).isNotNull();
        assertThat(result.getGroupName()).isEqualTo("soccerTeam1");
        verify(groupRepository, times(1)).findByGroupId(33L);
    }

    @Test
    public void getNonExistingGroupByIdTest() {
        when(groupRepository.findByGroupId(33L)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> {
            groupService.getGroupById(33L);
        });
    }

    @Test
    public void getGroupSizeTest() {
        when(groupRepository.findByGroupId(33L)).thenReturn(Optional.of(group1));

        assertThat(groupService.getGroupSizeById(33L)).isEqualTo(6);
        verify(groupRepository, times(1)).findByGroupId(33L);
    }

    @Test
    public void getGroupByGroupName() {
        when(groupRepository.findByGroupName("soccerTeam1")).thenReturn(Optional.of(group1));

        assertThat(groupService.getGroupByGroupName("soccerTeam1").getGroupId()).isEqualTo(33L);
        assertThat(groupService.getGroupByGroupName("soccerTeam1").getGroupSize()).isEqualTo(6L);
        verify(groupRepository, times(2)).findByGroupName("soccerTeam1");
    }

    @Test
    public void createGroupTest() {
        group3 = new Group("basketballTeam1", List.of(arslan, emma, nadine, panagiotis));
        when(groupRepository.save(group3)).thenReturn(group3);

        assertThat(groupService.createGroup("basketballTeam1")).isTrue();
        verify(groupRepository, times(1)).save(group3);
    }

    @Test
    public void createAlreadyExistingGroupTest () {
        when(groupRepository.findByGroupName("soccerTeam1")).thenReturn(Optional.of(group1));

        assertThrows(IllegalStateException.class, () ->
                groupService.createGroup("soccerTeam1"));
        verify(groupRepository, times(1)).findByGroupName("soccerTeam1");
    }

    @Test
    @Ignore
    public void addCustomerToGroupTest() {

        arslan.setId(1L);
        arslan.setGroupsForTeamSports(new ArrayList<>());

        when(customerService.getCustomerById(1L)).thenReturn(arslan);
        when(groupRepository.findByGroupId(42L)).thenReturn(Optional.of(group2));
        when(customerService.saveCustomer(arslan)).thenReturn(arslan);

        assertThat(groupService.addCustomerToGroup(1L, 42L).getGroupSize()).isEqualTo(4L);
        verify(groupRepository, times(1)).findByGroupId(42L);

    }

    @Test
    public void addAlreadyExistingCustomerToGroupTest() {
        arslan.setId(1L);
        arslan.setGroupsForTeamSports(new ArrayList<>());

        when(customerService.getCustomerById(1L)).thenReturn(arslan);
        when(groupRepository.findByGroupId(42L)).thenReturn(Optional.of(group2));
        when(customerService.saveCustomer(arslan)).thenReturn(arslan);

        assertThat(groupService.addCustomerToGroup(1L, 42L).getGroupSize()).isEqualTo(4L);
        assertThrows(IllegalStateException.class, () ->
                groupService.addCustomerToGroup(1L, 42L));

        verify(groupRepository, times(2)).findByGroupId(42L);
    }

    @Test
    @Ignore
    public void getUsersInAGroupTest() {
        when(groupRepository.findByGroupId(42L)).thenReturn(Optional.of(group2));
        assertThat(groupService.getUsersInAGroup(42L).size()).isEqualTo(3L);
    }


}
