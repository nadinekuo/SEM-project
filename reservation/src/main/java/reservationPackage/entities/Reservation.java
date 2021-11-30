package reservationPackage.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.apache.tomcat.jni.Local;

enum Type {
    EQUIPMENT, SPORTS_FACILITY, LESSON
}

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @SequenceGenerator(name = "reservation_sequence", sequenceName = "reservation_sequence",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservation_sequence")
    private long reservationId;


    private Type typeOfReservation; //equipment, lesson or sportroom
    private Long customerId;   // for group reservations, there will be separate reservations
    private Long sportFacilityReservedId;
    private Long equipmentId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startingTime;

/*    private ReservationSorting reservationSorting;

    public Reservation(ReservationSorting reservationSorting) {
        this.reservationSorting = reservationSorting;
    }*/

   //public void executeStrategy(ArrayList<Reservation> reservations);

    /**
     * Empty constructor needed for Spring JPA.
     */
    public Reservation() {
    }


    public Type getTypeOfReservation() {
        return typeOfReservation;
    }

    public void setTypeOfReservation(Type typeOfReservation) {
        this.typeOfReservation = typeOfReservation;
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

    public String getSportRoomReservedId() {
        return sportRoomReservedId;
    }

    public void setSportRoomReservedId(String sportRoomReservedId) {
        this.sportRoomReservedId = sportRoomReservedId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationId);
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
    public String toString() {
        return "Reservation{" + "reservationId=" + reservationId + ", startingTime=" + startingTime
            + ", timeSlot=" + timeSlot;
    }
}
