package user.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import user.entities.Customer;
import user.entities.Group;
import user.repositories.GroupRepository;

@ExtendWith(MockitoExtension.class)
public class GroupServiceTest {


    @Mock
    private transient GroupRepository groupRepository;

    private transient GroupService groupService;

    private transient Customer arslan;
    private transient Customer emil;
    private transient Customer emma;
    private transient Customer erwin;
    private transient Customer nadine;
    private transient Customer panagiotis;
    private transient Group group1;
    private transient Group group2;
    private transient Group group3;


    /**
     * runs before each test.
     */
    @BeforeEach
    void setup() {
        groupService = new GroupService(groupRepository);
    }

    public GroupServiceTest() {
        arslan = new Customer("arslan123", "password1", true);
        emil = new Customer("emil123", "password2", false);
        emma = new Customer("emma123", "password3", true);
        erwin = new Customer("erwin123", "password4", false);
        nadine = new Customer("nadine123", "password5", true);
        panagiotis = new Customer("panas123", "password6", false);
        group1 = new Group(33L, "soccerTeam1", List.of(arslan, emil, nadine, erwin, emma,
            panagiotis));
        group2 = new Group(42L, "volleyballTeam3", List.of(emma, panagiotis, erwin));
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
    public void getGroupById() {
        when(groupRepository.findByGroupId(33L)).thenReturn(Optional.of(group1));

        Group result = groupService.getGroupById(33L);

        assertThat(result).isNotNull();
        assertThat(result.getGroupName()).isEqualTo("soccerTeam1");
        verify(groupRepository, times(1)).findByGroupId(33L);
    }


    @Test
    public void getNonExistingGroupById() {
        when(groupRepository.findByGroupId(33L)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> {
            groupService.getGroupById(33L);
        });
    }


    @Test
    public void getGroupSize() {
        when(groupRepository.findByGroupId(33L)).thenReturn(Optional.of(group1));

        assertThat(groupService.getGroupSizeById(33L)).isEqualTo(6);
        verify(groupRepository, times(1)).findByGroupId(33L);
    }




}
