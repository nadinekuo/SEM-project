package reservationPackage.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @SequenceGenerator(name = "reservation_sequence", sequenceName = "reservation_sequence",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservation_sequence")
    private long reservationId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startingTime;

    private long timeSlot;  // in hours

    private Long customerId;   // for group reservations, there will be separate reservations
    private Long equipmentBorrowedId;  // for each piece reserved, there is a separate reservation
    private String sportRoomReservedId;


    /**
     *  Empty constructor needed for Spring JPA.
     */
    public Reservation() {
    }

    /** Constructor Reservation.
     *
     * @param reservationId - long
     * @param startingTime - LocalDateTime
     * @param timeSlot - long
     */
    public Reservation(long reservationId, LocalDateTime startingTime, long timeSlot) {
        this.reservationId = reservationId;
        this.startingTime = startingTime;
        this.timeSlot = timeSlot;
    }

    public long getReservationId() {
        return reservationId;
    }

    public void setReservationId(long reservationId) {
        this.reservationId = reservationId;
    }

    public LocalDateTime getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(LocalDateTime startingTime) {
        this.startingTime = startingTime;
    }

    public long getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(long timeSlot) {
        this.timeSlot = timeSlot;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getEquipmentBorrowedId() {
        return equipmentBorrowedId;
    }

    public void setEquipmentBorrowedId(Long equipmentBorrowedId) {
        this.equipmentBorrowedId = equipmentBorrowedId;
    }

    public String getSportRoomReservedId() {
        return sportRoomReservedId;
    }

    public void setSportRoomReservedId(String sportRoomReservedId) {
        this.sportRoomReservedId = sportRoomReservedId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation that = (Reservation) o;
        return reservationId == that.reservationId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationId);
    }

    @Override
    public String toString() {
        return "Reservation{" + "reservationId=" + reservationId + ", startingTime=" + startingTime
            + ", timeSlot=" + timeSlot;
    }
}
