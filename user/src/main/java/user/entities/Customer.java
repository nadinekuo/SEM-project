package user.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * The type Customer.
 */
@Entity
@Table(name = "customers")
public class Customer extends User {

    private boolean premiumSubscription;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_groups", joinColumns = {
        @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, updatable =
            false)
    }, inverseJoinColumns = {
        @JoinColumn(name = "group_id", referencedColumnName = "groupId", nullable = false,
            updatable = false)
    })
    @JsonBackReference
    @JsonIgnoreProperties("groupMembers")
    private List<Group> groupsForTeamSports;

    /**
     * Instantiates a new Customer.
     */
    public Customer() {
    }

    /**
     * Instantiates a new Customer.
     *
     * @param username            the username
     * @param password            the password
     * @param premiumSubscription the premium subscription
     */
    public Customer(String username, String password, boolean premiumSubscription) {
        super(username, password);
        this.premiumSubscription = premiumSubscription;
    }

    /**
     * Instantiates a new Customer.
     *
     * @param id                  the id
     * @param username            the username
     * @param password            the password
     * @param premiumSubscription the premium subscription
     * @param groupsForTeamSports the groups for team sports
     */
    public Customer(long id, String username, String password, boolean premiumSubscription,
                    List<Group> groupsForTeamSports) {
        super(id, username, password);
        this.premiumSubscription = premiumSubscription;
        this.groupsForTeamSports = groupsForTeamSports;
    }

    /**
     * Is premium user boolean.
     *
     * @return the boolean
     */
    public boolean isPremiumUser() {
        return premiumSubscription;
    }

    /**
     * Sets premium user.
     *
     * @param premiumUser the premium user
     */
    public void setPremiumUser(boolean premiumUser) {
        this.premiumSubscription = premiumUser;
    }

    /**
     * Gets groups for team sports.
     *
     * @return the groups for team sports
     */
    public List<Group> getGroupsForTeamSports() {
        return groupsForTeamSports;
    }

    /**
     * Sets groups for team sports.
     *
     * @param groupsForTeamSports the groups for team sports
     */
    public void setGroupsForTeamSports(List<Group> groupsForTeamSports) {
        this.groupsForTeamSports = groupsForTeamSports;
    }

    //    public boolean isPremiumSubscription() {
    //        return premiumSubscription;
    //    }

    //    public void setPremiumSubscription(boolean premiumSubscription) {
    //        this.premiumSubscription = premiumSubscription;
    //    }

    /**
     * Add group to users group list.
     *
     * @param group the group
     */
    public void addGroupToUsersGroupList(Group group) {
        if (!groupsForTeamSports.contains(group)) {
            this.groupsForTeamSports.add(group);
            group.setGroupSize(group.getGroupSize() + 1);
        } else {
            throw new IllegalStateException(
                "customer with id " + this.getId() + " already exists in the group!");
        }
    }

    @Override
    public String toString() {
        String res =
            "Customer{" + "id=" + super.getId() + ", username='" + super.getUsername() + '\''
                + ", password" + "='" + super.getPassword() + "', ";

        if (!groupsForTeamSports.isEmpty()) {
            res = res + " groups = {";
            for (Group g : groupsForTeamSports) {
                res = res + "'" + g.getGroupName() + "'" + ",";
            }
            res = res + "}";
            return res;
        }
        return res + "}";
    }

}
