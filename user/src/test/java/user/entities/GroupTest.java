package user.entities;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class GroupTest {

    private final transient Customer arslan;
    private final transient Customer emil;
    private final transient Customer emma;
    private final transient Customer erwin;
    private final transient Customer nadine;
    private final transient Customer panagiotis;
    private final transient Group group1;
    private final transient Group group2;

    /**
     * Constructor for test.
     */
    public GroupTest() {
        arslan = new Customer("arslan123", "password1", true);
        emil = new Customer("emil123", "password2", false);
        emma = new Customer("emma123", "password3", true);
        erwin = new Customer("erwin123", "password4", false);
        nadine = new Customer("nadine123", "password5", true);
        panagiotis = new Customer("panas123", "password6", false);

        group1 =
            new Group(33L, "soccerTeam1", List.of(arslan, emil, nadine, erwin, emma, panagiotis));
        group2 = new Group(42L, "volleyballTeam3", List.of(emma, panagiotis, erwin));
    }

    @Test
    void setGroupId() {
        group1.setGroupId(56L);
        assertThat(group1.getGroupId()).isEqualTo(56L);
    }

    @Test
    void setGroupName() {
        group1.setGroupName("basketballTeam1");
        assertThat(group1.getGroupName()).isEqualTo("basketballTeam1");
    }

    @Test
    void getGroupMembers() {
        List<Customer> customers = group2.getGroupMembers();
        for (int i = 0; i < customers.size(); i++) {
            if (i == 0) {
                assertThat(customers.get(i).getUsername()).isEqualTo("emma123");
            } else if (i == 1) {
                assertThat(customers.get(i).getUsername()).isEqualTo("panas123");
            } else {
                assertThat(customers.get(i).getUsername()).isEqualTo("erwin123");
            }
        }
        assertThat(group2.getGroupSize()).isEqualTo(3L);
    }

    @Test
    void setGroupMembers() {
        List<Customer> newCustomerList = Arrays.asList(arslan, emil, nadine);

        group2.setGroupMembers(newCustomerList);

        for (int i = 0; i < group2.getGroupMembers().size(); i++) {
            if (i == 0) {
                assertThat(group2.getGroupMembers().get(i).getUsername()).isEqualTo("arslan123");
            } else if (i == 1) {
                assertThat(group2.getGroupMembers().get(i).getUsername()).isEqualTo("emil123");
            } else {
                assertThat(group2.getGroupMembers().get(i).getUsername()).isEqualTo("nadine123");
            }
        }
    }

    @Test
    void setGroupSize() {
        group2.setGroupSize(6);
        assertThat(group2.getGroupSize()).isEqualTo(6);
    }

    //    @Test
    //    void addUserToGroup() {
    //        //arslan.setGroupsForTeamSports(new ArrayList<>());
    //        //arslan.addGroupToUsersGroupList(group2);
    //        group2.addUserToGroup(arslan);
    //        assertThat(group2.getGroupSize()).isEqualTo(4L);
    //    }
}