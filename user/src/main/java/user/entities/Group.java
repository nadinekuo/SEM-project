package user.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;


//TODO remove a constructor
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
     * Empty constructor needed for Spring JPA.
     */
    public Group() {
    }

    /**
     * Constructor Group.
     *
     * @param groupId      - long
     * @param groupName    - String
     * @param groupMembers - List<User></User>
     */
    public Group(long groupId, String groupName, List<Customer> groupMembers) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupMembers = groupMembers;
        this.groupSize = groupMembers.size();
    }

    public Group(String groupName, List<Customer> groupMembers) {
        this.groupName = groupName;
        this.groupMembers = groupMembers;
        this.groupSize = groupMembers.size();
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<Customer> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(List<Customer> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public int getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(int groupSize) {
        this.groupSize = groupSize;
    }

    /**
     * A new member is added to this group, to be referenced to later when reserving team sport
     * fields/halls.
     *
     * @param memberToAdd - User to be added to this group.
     */
    public void addUserToGroup(Customer memberToAdd) {
        if (!groupMembers.contains(memberToAdd)) {
            this.groupMembers.add(memberToAdd);
            this.groupSize++;
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


