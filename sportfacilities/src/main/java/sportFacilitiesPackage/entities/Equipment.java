package sportFacilitiesPackage.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "equipment")
public class Equipment {

    @Id
    @SequenceGenerator(name = "equipment_sequence", sequenceName = "equipment_sequence",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "equipment_sequence")
    private long equipmentId;

    private String name;

    private boolean isInUse;       // available = inventory - inUse

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "sport_name", nullable = false)
    @JsonBackReference
    private Sport relatedSport;

    /**
     * Empty constructor needed for Spring JPA.
     */
    public Equipment() {
    }

    public Equipment(long equipmentId, String name, boolean isInUse, Sport relatedSport) {
        this.equipmentId = equipmentId;
        this.name = name;
        this.isInUse = isInUse;
        this.relatedSport = relatedSport;
    }

    public boolean isInUse() {
        return isInUse;
    }

    public void setInUse(boolean inUse) {
        isInUse = inUse;
    }

    public long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sport getRelatedSport() {
        return relatedSport;
    }

    public void setRelatedSport(Sport relatedSport) {
        this.relatedSport = relatedSport;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Equipment equipment = (Equipment) o;
        return Objects.equals(name, equipment.name);
    }

    @Override
    public String toString() {
        return "Equipment{" + "equipmentId=" + equipmentId + ", name='" + name + '\'' + ", isInUse="
            + isInUse + ", relatedSport=" + relatedSport + '}';
    }
}
