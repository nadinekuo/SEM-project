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

/**
 * The type Sport.
 */
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

    /**
     * Instantiates a new Sport.
     */
    public Sport() {
    }



    /**
     * Instantiates a new Sport.
     *
     * @param sportName   the sport name
     * @param minTeamSize the min team size
     * @param maxTeamSize the max team size
     */
    public Sport(String sportName,  int minTeamSize, int maxTeamSize) {
        this.sportName = sportName;
        this.teamSport = true;
        this.minTeamSize = minTeamSize;
        this.maxTeamSize = maxTeamSize;
    }

    /**
     * Instantiates a new Sport.
     *
     * @param sportName the sport name
     */
    public Sport(String sportName) {
        this.sportName = sportName;
        this.teamSport = false;
        this.minTeamSize = 1;
        this.maxTeamSize = -1;
    }

    /**
     * Gets sport name.
     *
     * @return the sport name
     */
    public String getSportName() {
        return sportName;
    }

    /**
     * Sets sport name.
     *
     * @param sportName the sport name
     */
    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    /**
     * Is team sport boolean.
     *
     * @return the boolean
     */
    public boolean isTeamSport() {
        return teamSport;
    }

    /**
     * Sets team sport.
     *
     * @param teamSport the team sport
     */
    public void setTeamSport(boolean teamSport) {
        this.teamSport = teamSport;
    }

    /**
     * Gets sport locations.
     *
     * @return the sport locations
     */
    public List<SportRoom> getSportLocations() {
        return sportLocations;
    }

    /**
     * Sets sport locations.
     *
     * @param sportLocations the sport locations
     */
    public void setSportLocations(List<SportRoom> sportLocations) {
        this.sportLocations = sportLocations;
    }

    /**
     * Gets equipment list.
     *
     * @return the equipment list
     */
    public List<Equipment> getEquipmentList() {
        return equipmentList;
    }

    /**
     * Sets equipment list.
     *
     * @param equipmentList the equipment list
     */
    public void setEquipmentList(List<Equipment> equipmentList) {
        this.equipmentList = equipmentList;
    }

    /**
     * Gets min team size.
     *
     * @return the min team size
     */
    public int getMinTeamSize() {
        return minTeamSize;
    }

    /**
     * Sets min team size.
     *
     * @param minTeamSize the min team size
     */
    public void setMinTeamSize(int minTeamSize) {
        this.minTeamSize = minTeamSize;
    }

    /**
     * Gets max team size.
     *
     * @return the max team size
     */
    public int getMaxTeamSize() {
        return maxTeamSize;
    }

    /**
     * Sets max team size.
     *
     * @param maxTeamSize the max team size
     */
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
