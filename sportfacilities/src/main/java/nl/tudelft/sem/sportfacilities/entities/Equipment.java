package nl.tudelft.sem.sportfacilities.entities;

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
    private Long equipmentId;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "sport_name", nullable = false)
    @JsonBackReference
    private Sport relatedSport;

    private boolean inUse;

    /**
     * Empty constructor needed for Spring JPA.
     */
    public Equipment() {
    }

    /**
     * Instantiates a new Equipment.
     *
     * @param equipmentId  id
     * @param name         the name
     * @param relatedSport the related sport
     * @param inUse        if its in use
     */
    public Equipment(Long equipmentId, String name, Sport relatedSport, boolean inUse) {
        this.equipmentId = equipmentId;
        this.name = name;
        this.relatedSport = relatedSport;
        this.inUse = inUse;
    }

    /**
     * Instantiates a new Equipment.
     *
     * @param name         the name
     * @param relatedSport the related sport
     * @param inUse        if its in use
     */
    public Equipment(String name, Sport relatedSport, boolean inUse) {
        this.name = name;
        this.relatedSport = relatedSport;
        this.inUse = inUse;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    public Long getEquipmentId() {
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
        return "Equipment{" + "equipmentId=" + equipmentId + ", name='" + name + "'"
            + ", relatedSport=" + relatedSport.getSportName() + '}';
    }
}
