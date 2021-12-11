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
    private Long reservationId;

    private ReservationType typeOfReservation;    // 0 = Equipment, 1 = SportRoom, 2 = Lesson
    private Long customerId;   // for group reservations, there will be separate reservations
    private Long groupId;     // will be -1 if it's not a group reservation
    private Long sportFacilityReservedId;   // EquipmentId, LessonId or sportRoomId

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startingTime;

    private int timeSlotInMinutes;

    /**
     * Instantiates a new Reservation with id.
     *
     * @param id                      reservation id
     * @param typeOfReservation       the type of reservation
     * @param customerId              the customer id
     * @param sportFacilityReservedId the sport facility reserved id
     * @param startingTime            the starting time
     */
    public Reservation(Long id, ReservationType typeOfReservation, Long customerId,
                       Long sportFacilityReservedId, LocalDateTime startingTime) {

        this.reservationId = id;
        this.typeOfReservation = typeOfReservation;
        this.customerId = customerId;
        this.sportFacilityReservedId = sportFacilityReservedId;
        this.startingTime = startingTime;
        this.timeSlotInMinutes = 60;   // Default time slot for Equipment / Sport room reservations
        this.groupId = -1L;    // If not a group reservation
    }

    /**
     * Instantiates a new Reservation.
     *
     * @param id                      reservation id
     * @param typeOfReservation       the type of reservation
     * @param customerId              the customer id
     * @param sportFacilityReservedId the sport facility reserved id
     * @param startingTime            the starting time
     * @param groupId                 id of group that is associated with this reservation, will
     *                                be -1L if this is not a group reservation.
     */
    public Reservation(Long id, ReservationType typeOfReservation, Long customerId,
                       Long sportFacilityReservedId, LocalDateTime startingTime, Long groupId) {

        this.reservationId = id;
        this.typeOfReservation = typeOfReservation;
        this.customerId = customerId;
        this.sportFacilityReservedId = sportFacilityReservedId;
        this.startingTime = startingTime;
        this.timeSlotInMinutes = 60;
        this.groupId = groupId;
    }

    /**
     * Instantiates a new Reservation.
     *
     * @param typeOfReservation       the type of reservation
     * @param customerId              the customer id
     * @param sportFacilityReservedId the sport facility reserved id
     * @param startingTime            the starting time
     */
    public Reservation(ReservationType typeOfReservation, Long customerId,
                       Long sportFacilityReservedId, LocalDateTime startingTime) {

        this.typeOfReservation = typeOfReservation;
        this.customerId = customerId;
        this.sportFacilityReservedId = sportFacilityReservedId;
        this.startingTime = startingTime;
        this.timeSlotInMinutes = 60;   // Default time slot for Equipment / Sport room reservations
        this.groupId = -1L;    // If not a group reservation
    }

    /**
     * Instantiates a new Reservation.
     *
     * @param typeOfReservation       the type of reservation
     * @param customerId              the customer id
     * @param sportFacilityReservedId the sport facility reserved id
     * @param startingTime            the starting time
     * @param timeSlotInMinutes       for how long the equipment/sport room can be used, or the
     *                                lesson duration
     */
    public Reservation(ReservationType typeOfReservation, Long customerId,
                       Long sportFacilityReservedId, LocalDateTime startingTime,
                       int timeSlotInMinutes) {

        this.typeOfReservation = typeOfReservation;
        this.customerId = customerId;
        this.sportFacilityReservedId = sportFacilityReservedId;
        this.startingTime = startingTime;
        this.timeSlotInMinutes = timeSlotInMinutes;
        this.groupId = -1L;
    }

    /**
     * Instantiates a new Reservation.
     *
     * @param typeOfReservation       the type of reservation
     * @param customerId              the customer id
     * @param sportFacilityReservedId the sport facility reserved id
     * @param startingTime            the starting time
     * @param groupId                 id of group that is associated with this reservation, will
     *                                be -1L if this is not a group reservation.
     */
    public Reservation(ReservationType typeOfReservation, Long customerId,
                       Long sportFacilityReservedId, LocalDateTime startingTime, Long groupId) {

        this.typeOfReservation = typeOfReservation;
        this.customerId = customerId;
        this.sportFacilityReservedId = sportFacilityReservedId;
        this.startingTime = startingTime;
        this.timeSlotInMinutes = 60;
        this.groupId = groupId;
    }

    /**
     * Empty constructor needed for Spring JPA.
     */
    public Reservation() {
    }

    public Long getId() {
        return reservationId;
    }

    public Long getSportFacilityReservedId() {
        return sportFacilityReservedId;
    }

    /*    private ReservationSorting reservationSorting;

    public Reservation(ReservationSorting reservationSorting) {
        this.reservationSorting = reservationSorting;
    }*/

    //public void executeStrategy(ArrayList<Reservation> reservations);

    public void setSportFacilityReservedId(Long sportFacilityReservedId) {
        this.sportFacilityReservedId = sportFacilityReservedId;
    }

    public ReservationType getTypeOfReservation() {
        return typeOfReservation;
    }

    public void setTypeOfReservation(ReservationType typeOfReservation) {
        this.typeOfReservation = typeOfReservation;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
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

    public int getTimeSlotInMinutes() {
        return timeSlotInMinutes;
    }

    public void setTimeSlotInMinutes(int timeSlotInMinutes) {
        this.timeSlotInMinutes = timeSlotInMinutes;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
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
        return "Reservation{" + "reservationId=" + reservationId + ", typeOfReservation="
            + typeOfReservation + ", customerId=" + customerId + ", groupId=" + groupId
            + ", startingTime=" + startingTime + '}';
    }
}
