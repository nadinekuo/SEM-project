package nl.tudelft.sem.template.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "equipment")
public class Equipment {

    @Id
    private String name;  // example: "hockeystick1"
    private int inventory;
    private int inUse;     // available = inventory - inUse

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "sport_name", nullable = false)
    @JsonBackReference
    private Sport relatedSport;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id", nullable = false)
    @JsonBackReference
    private User borrower;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "reservation_id", nullable = false)
    @JsonBackReference
    private Reservation reservation;

    /**
     *  Empty constructor needed for Spring JPA.
     */
    public Equipment() {
    }

    /** Constructor Equipment.
     *
     * @param name - String
     * @param inventory - int
     * @param inUse - int
     * @param relatedSport - Sport
     */
    public Equipment(String name, int inventory, int inUse, Sport relatedSport) {
        this.name = name;
        this.inventory = inventory;
        this.inUse = inUse;
        this.relatedSport = relatedSport;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public int getInUse() {
        return inUse;
    }

    public void setInUse(int inUse) {
        this.inUse = inUse;
    }

    public Sport getRelatedSport() {
        return relatedSport;
    }

    public void setRelatedSport(Sport relatedSport) {
        this.relatedSport = relatedSport;
    }

    public User getBorrower() {
        return borrower;
    }

    public void setBorrower(User borrower) {
        this.borrower = borrower;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
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
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Equipment{" + "name='" + name + '\'' + ", inventory=" + inventory + ", inUse="
            + inUse + ", relatedSport=" + relatedSport + '}';
    }
}
