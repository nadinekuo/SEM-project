package user.entities;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerTest {

    private final transient Customer arslan;
    private final transient Customer emil;
    private final transient Customer emma;
    private final transient Customer erwin;
    private final transient Customer nadine;
    private final transient Customer panagiotis;
    private final transient Group group1;
    private final transient Group group2;

    public CustomerTest() {
        arslan = new Customer("arslan123", "password1", true);
        emil = new Customer("emil123", "password2", false);
        emma = new Customer("emma123", "password3", true);
        erwin = new Customer("erwin123", "password4", false);
        nadine = new Customer("nadine123", "password5", true);
        panagiotis = new Customer("panas123", "password6", false);

        group1 = new Group(33L, "soccerTeam1", List.of(arslan, emil, nadine, erwin, emma, panagiotis));
        group2 = new Group(42L, "volleyballTeam3", List.of(emma, panagiotis, erwin));
    }


    @Test
    void isPremiumUser() {
        assertThat(arslan.isPremiumUser()).isTrue();
        assertThat(emil.isPremiumUser()).isFalse();
    }

    @Test
    void setPremiumUser() {
        arslan.setPremiumUser(false);
        assertThat(arslan.isPremiumUser()).isFalse();

        emil.setPremiumUser(true);
        assertThat(emil.isPremiumUser()).isTrue();
    }

    @Test
    void getGroupsForTeamSports() {
        arslan.setGroupsForTeamSports(new ArrayList<>());
        assertThat(arslan.getGroupsForTeamSports().isEmpty()).isTrue();
        arslan.addGroupToUsersGroupList(group1);
        arslan.addGroupToUsersGroupList(group2);

        assertThat(arslan.getGroupsForTeamSports().size()).isEqualTo(2);

        assertThat(arslan.getGroupsForTeamSports().contains(group1)).isTrue();
        assertThat(arslan.getGroupsForTeamSports().contains(group2)).isTrue();

    }

    @Test
    void setGroupsForTeamSports() {
        arslan.setGroupsForTeamSports(Arrays.asList(group1, group2));
        assertThat(arslan.getGroupsForTeamSports().size()).isEqualTo(2);
    }

    @Test
    void addGroupToUsersGroupList() {
        arslan.setGroupsForTeamSports(new ArrayList<>());
        arslan.addGroupToUsersGroupList(group1);
        assertThat(arslan.getGroupsForTeamSports().contains(group1)).isTrue();
    }

//    @Test
//    void testToString() {
//        arslan.setId(1l);
//        arslan.setGroupsForTeamSports(new ArrayList<>());
//        arslan.addGroupToUsersGroupList(group1);
//
//        String temp = "Customer{id=1, username='arslan123"
//    }
}