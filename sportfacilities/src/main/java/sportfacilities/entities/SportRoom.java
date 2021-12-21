package sportfacilities.entities;

// Can be either a:
// Sport hall: different sports can be exercised here
// Sport field: specific to a certain sport (soccer, hockey e.g.)

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "sportroom")
public class SportRoom {

    boolean isSportsHall;
    @Id
    @SequenceGenerator(name = "sportroom_sequence", sequenceName = "sportroom_sequence",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sportroom_sequence")
    private Long sportRoomId;
    private String sportRoomName; // example: X1, X2, X3 ...

    @ManyToMany(mappedBy = "sportLocations", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Sport> sports;   // Only sport halls will store multiple sports
    private int minCapacity;
    private int maxCapacity;


    /**
     * Empty constructor needed for Spring JPA.
     */
    public SportRoom() {
    }

    /**
     * Constructor SportRoom.
     *
     * @param sportRoomName - String
     * @param sports        - the associated sports
     * @param minCapacity   - int
     * @param maxCapacity   - int
     */
    public SportRoom(String sportRoomName, List<Sport> sports, int minCapacity, int maxCapacity) {
        this.sportRoomName = sportRoomName;
        this.sports = sports;
        this.minCapacity = minCapacity;
        this.maxCapacity = maxCapacity;
        this.isSportsHall = sports.size() > 1;
    }

    /**
     * Constructor SportRoom.
     *
     * @param sportRoomId   - Long
     * @param sportRoomName - String
     * @param sports        - the associated sports
     * @param minCapacity   - int
     * @param maxCapacity   - int
     */
    public SportRoom(long sportRoomId, String sportRoomName, List<Sport> sports, int minCapacity,
                     int maxCapacity) {
        this.isSportsHall = sports.size() > 1;
        this.sportRoomId = sportRoomId;
        this.sportRoomName = sportRoomName;
        this.sports = sports;
        this.minCapacity = minCapacity;
        this.maxCapacity = maxCapacity;
    }

    public Long getSportRoomId() {
        return sportRoomId;
    }

    public void setSportRoomId(long sportRoomId) {
        this.sportRoomId = sportRoomId;
    }

    public boolean getIsSportsHall() {
        return isSportsHall;
    }

    public void setIsSportsHall(boolean sportsHall) {
        isSportsHall = sportsHall;
    }

    public String getSportRoomName() {
        return sportRoomName;
    }

    public void setSportRoomName(String sportRoomName) {
        this.sportRoomName = sportRoomName;
    }

    public List<Sport> getSports() {
        return sports;
    }

    public void setSports(List<Sport> sports) {
        this.sports = sports;
    }

    public int getMinCapacity() {
        return minCapacity;
    }

    public void setMinCapacity(int minCapacity) {
        this.minCapacity = minCapacity;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public boolean isSportsHall() {
        return isSportsHall;
    }

    public void setSportsHall(boolean sportsHall) {
        isSportsHall = sportsHall;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sportRoomName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SportRoom sportRoom = (SportRoom) o;
        return Objects.equals(sportRoomName, sportRoom.sportRoomName);
    }

    @Override
    public String toString() {
        return "SportRoom{" + "sportRoomName='" + sportRoomName + '\'' + ", sports=" + sports
            + ", minCapacity=" + minCapacity + ", maxCapacity=" + maxCapacity;
    }
}
