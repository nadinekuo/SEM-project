package user.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * The type Group.
 */
@Entity
@Table(name = "groups")
public class Group {

    @Id
    @SequenceGenerator(name = "group_sequence", sequenceName = "group_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_sequence")
    private long groupId;

    private String groupName;
    private int groupSize;

    @ManyToMany(mappedBy = "groupsForTeamSports", fetch = FetchType.EAGER)
    @JsonManagedReference
    @JsonIgnoreProperties("groupForTeamSports")
    private List<Customer> groupMembers;

    /**
     * Instantiates a new Group.
     */
    public Group() {
    }

    /**
     * Instantiates a new Group.
     *
     * @param groupId      the group id
     * @param groupName    the group name
     * @param groupMembers the group members
     */
    public Group(long groupId, String groupName, List<Customer> groupMembers) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupMembers = groupMembers;
        this.groupSize = groupMembers.size();
    }

    /**
     * Instantiates a new Group.
     *
     * @param groupName    the group name
     * @param groupMembers the group members
     */
    public Group(String groupName, List<Customer> groupMembers) {
        this.groupName = groupName;
        this.groupMembers = groupMembers;
        this.groupSize = groupMembers.size();
    }

    /**
     * Gets group id.
     *
     * @return the group id
     */
    public long getGroupId() {
        return groupId;
    }

    /**
     * Sets group id.
     *
     * @param groupId the group id
     */
    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    /**
     * Gets group name.
     *
     * @return the group name
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Sets group name.
     *
     * @param groupName the group name
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * Gets group members.
     *
     * @return the group members
     */
    public List<Customer> getGroupMembers() {
        return groupMembers;
    }

    /**
     * Sets group members.
     *
     * @param groupMembers the group members
     */
    public void setGroupMembers(List<Customer> groupMembers) {
        this.groupMembers = groupMembers;
    }

    /**
     * Gets group size.
     *
     * @return the group size
     */
    public int getGroupSize() {
        return groupSize;
    }

    /**
     * Sets group size.
     *
     * @param groupSize the group size
     */
    public void setGroupSize(int groupSize) {
        this.groupSize = groupSize;
    }

    /**
     * Add user to group.
     *
     * @param memberToAdd the member to add
     */
    public void addUserToGroup(Customer memberToAdd) {
        if (!this.groupMembers.contains(memberToAdd)) {
            this.groupMembers.add(memberToAdd);
            this.groupSize++;
        } else {
            throw new IllegalStateException(
                "Customer with id : " + memberToAdd.getId() + " already exists in the group!");
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Group group = (Group) o;
        return groupId == group.groupId;
    }

    @Override
    public String toString() {
        return "Group{" + "groupId=" + groupId + ", groupName='" + groupName + '\''
            + ", groupMembers=" + groupMembers + '}';
    }
}


