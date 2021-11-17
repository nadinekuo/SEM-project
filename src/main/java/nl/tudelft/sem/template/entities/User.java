package nl.tudelft.sem.template.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    private long id;
    private String username;
    private String password;   // Spring Security
    private boolean premiumUser;


    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_groups",
        joinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName = "id",
                nullable = false, updatable = false)},
        inverseJoinColumns = {
            @JoinColumn(name = "group_id", referencedColumnName = "groupId",
                nullable = false, updatable = false)})
    private List<Group> groupsForTeamSports;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "lesson_attendees",
        joinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName = "id",
                nullable = false, updatable = false)},
        inverseJoinColumns = {
            @JoinColumn(name = "lesson_id", referencedColumnName = "lessonId",
                nullable = false, updatable = false)})
    private List<Lesson> lessonsBooked;

    @OneToMany(mappedBy = "borrower", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<Equipment> equipmentBorrowed;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "reservation_users",
        joinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName = "id",
                nullable = false, updatable = false)},
        inverseJoinColumns = {
            @JoinColumn(name = "reservation_id", referencedColumnName = "reservationId",
                nullable = false, updatable = false)})
    private List<Reservation> reservations;



    /**
     *  Empty constructor needed for Spring JPA.
     */
    public User() {

    }

    /** Constructor User.
     *
     * @param id - Long
     * @param username - String
     * @param password - String
     * @param premiumUser - boolean
     */
    public User(long id, String username, String password, boolean premiumUser) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.premiumUser = premiumUser;
    }

    /** Constructor User without id.
     *
     * @param username - String
     * @param password - String
     * @param premiumUser - boolean
     */
    public User(String username, String password, boolean premiumUser) {
        this.username = username;
        this.password = password;
        this.premiumUser = premiumUser;
    }

    public long getId() {
        return id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPremiumUser(boolean premiumUser) {
        this.premiumUser = premiumUser;
    }

    public boolean isPremiumUser() {
        return premiumUser;
    }

    public List<Group> getGroupsForTeamSports() {
        return groupsForTeamSports;
    }

    public void setGroupsForTeamSports(List<Group> groupsForTeamSports) {
        this.groupsForTeamSports = groupsForTeamSports;
    }

    public List<Lesson> getLessonsBooked() {
        return lessonsBooked;
    }

    public void setLessonsBooked(List<Lesson> lessonsBooked) {
        this.lessonsBooked = lessonsBooked;
    }

    public List<Equipment> getEquipmentBorrowed() {
        return equipmentBorrowed;
    }

    public void setEquipmentBorrowed(List<Equipment> equipmentBorrowed) {
        this.equipmentBorrowed = equipmentBorrowed;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username='" + username + '\'' + ", password='" + password
            + '\'' + ", premiumUser=" + premiumUser + '}';
    }
}
