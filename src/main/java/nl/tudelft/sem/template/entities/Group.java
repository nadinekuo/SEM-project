package nl.tudelft.sem.template.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "groups")
public class Group {

    @Id
    @SequenceGenerator(
        name = "group_sequence",
        sequenceName = "group_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "group_sequence"
    )
    private long groupId;

    private String groupName;

    @ManyToMany(mappedBy = "groupsForTeamSports", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Customer> groupMembers;



    /**
     *  Empty constructor needed for Spring JPA.
     */
    public Group() {
    }

    /** Constructor Group.
     *
     * @param groupId - long
     * @param groupName - String
     * @param groupMembers - List<User></User>
     */
    public Group(long groupId, String groupName, List<Customer> groupMembers) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupMembers = groupMembers;
    }


    public Group(String groupName, List<Customer> groupMembers) {
        this.groupName = groupName;
        this.groupMembers = groupMembers;
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


    /** A new member is added to this group, to be referenced to later when reserving team sport
     *  fields/halls.
     *
     * @param memberToAdd - User to be added to this group.
     */
    public void addUserToGroup(Customer memberToAdd) {
        if (!groupMembers.contains(memberToAdd)) {
            this.groupMembers.add(memberToAdd);
        }
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
    public int hashCode() {
        return Objects.hash(groupId);
    }

    @Override
    public String toString() {
        return "Group{" + "groupId=" + groupId + ", groupName='" + groupName + '\''
            + ", groupMembers=" + groupMembers + '}';
    }
}


