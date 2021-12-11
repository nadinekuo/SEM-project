package reservation.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @SequenceGenerator(name = "reservation_sequence", sequenceName = "reservation_sequence",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservation_sequence")
    private long reservationId;

    private ReservationType typeOfReservation;    // 0 = Equipment, 1 = SportRoom, 2 = Lesson
    private Long customerId;   // for group reservations, there will be separate reservations
    private Long sportFacilityReservedId;   // EquipmentId, LessonId or sportRoomId
    private boolean isCombined;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startingTime;

    /**
     * Instantiates a new Reservation.
     *
     * @param typeOfReservation       the type of reservation
     * @param customerId              the customer id
     * @param sportFacilityReservedId the sport facility reserved id
     * @param startingTime            the starting time
     * @param isCombined              if it's a combined reservation
     */
    public Reservation(ReservationType typeOfReservation, Long customerId,
                       Long sportFacilityReservedId, LocalDateTime startingTime,
                       boolean isCombined) {

        this.typeOfReservation = typeOfReservation;
        this.customerId = customerId;
        this.sportFacilityReservedId = sportFacilityReservedId;
        this.startingTime = startingTime;
        this.isCombined = isCombined;
    }

    /**
     * Empty constructor needed for Spring JPA.
     */
    public Reservation() {
    }

    public Long getSportFacilityReservedId() {
        return sportFacilityReservedId;
    }


    public void setSportFacilityReservedId(Long sportFacilityReservedId) {
        this.sportFacilityReservedId = sportFacilityReservedId;
    }

    public ReservationType getTypeOfReservation() {
        return typeOfReservation;
    }

    public void setTypeOfReservation(ReservationType typeOfReservation) {
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

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public boolean getIsCombined() {
        return isCombined;
    }

    public void setIsCombined(boolean combined) {
        isCombined = combined;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationId);
    }

    @Override
    public String toString() {
        return "Reservation{" + "reservationId=" + reservationId + ", typeOfReservation="
            + typeOfReservation + ", customerId=" + customerId + ", sportFacilityReservedId="
            + sportFacilityReservedId + ", startingTime=" + startingTime + '}';
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

}
