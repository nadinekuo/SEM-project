package sportfacilities.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "sports")
public class Sport {

    @Id
    private String sportName;

    private boolean teamSport;
    private int minTeamSize;   //  1 if not team sport
    private int maxTeamSize;   //  -1 if not team sport

    @Transient
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "sport_locations", joinColumns = {
        @JoinColumn(name = "sport_name", referencedColumnName = "sportName", nullable = false,
            updatable = false)
        }, inverseJoinColumns = {
        @JoinColumn(name = "sportroom_name", referencedColumnName = "sportRoomName", nullable =
            false, updatable = false)
    })
    private List<SportRoom> sportLocations;

    @Transient
    @OneToMany(mappedBy = "relatedSport", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<Equipment> equipmentList;

    public Sport() {
    }

    /**
     * Constructor.
     *
     * @param sportName   - String, ID
     * @param teamSport   - boolean
     * @param minTeamSize - int, constraint for group reservations: > 1 if teamSport = true, else 1
     */
    public Sport(String sportName, boolean teamSport, int minTeamSize, int maxTeamSize) {
        this.sportName = sportName;
        this.teamSport = teamSport;
        this.minTeamSize = minTeamSize;
        this.maxTeamSize = maxTeamSize;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public boolean isTeamSport() {
        return teamSport;
    }

    public void setTeamSport(boolean teamSport) {
        this.teamSport = teamSport;
    }

    public List<SportRoom> getSportLocations() {
        return sportLocations;
    }

    public void setSportLocations(List<SportRoom> sportLocations) {
        this.sportLocations = sportLocations;
    }

    public List<Equipment> getEquipmentList() {
        return equipmentList;
    }

    public void setEquipmentList(List<Equipment> equipmentList) {
        this.equipmentList = equipmentList;
    }

    public int getMinTeamSize() {
        return minTeamSize;
    }

    public void setMinTeamSize(int minTeamSize) {
        this.minTeamSize = minTeamSize;
    }

    public int getMaxTeamSize() {
        return maxTeamSize;
    }

    public void setMaxTeamSize(int maxTeamSize) {
        this.maxTeamSize = maxTeamSize;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sportName, teamSport);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sport sport = (Sport) o;
        return teamSport == sport.teamSport && Objects.equals(sportName, sport.sportName);
    }

    @Override
    public String toString() {
        return "" + sportName + "";
    }
}
