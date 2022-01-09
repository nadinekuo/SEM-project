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
    private String bookedItemName;
    // Name of reservation, it can be Sport Hall or Equipment name or
    // Lesson title
    private Long customerId;   // for group reservations, there will be separate reservations
    private Long groupId;     // will be -1 if it's not a group reservation
    private Long sportFacilityReservedId;   // EquipmentId, LessonId or sportRoomId

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startingTime;

    private int timeSlotInMinutes;
    private Boolean madeByPremiumUser;

    /**
     * Instantiates a new Reservation.
     *
     * @param typeOfReservation       the type of reservation
     * @param bookedItemName          the booked item name
     * @param customerId              the customer id
     * @param sportFacilityReservedId the sport facility reserved id
     * @param startingTime            the starting time
     * @param madeByPremiumUser       if its made by a premium user
     */
    public Reservation(ReservationType typeOfReservation, String bookedItemName, Long customerId,
                       Long sportFacilityReservedId, LocalDateTime startingTime,
                       Boolean madeByPremiumUser) {

        this.typeOfReservation = typeOfReservation;
        this.bookedItemName = bookedItemName;
        this.customerId = customerId;
        this.sportFacilityReservedId = sportFacilityReservedId;
        this.startingTime = startingTime;
        this.madeByPremiumUser = madeByPremiumUser;
        this.timeSlotInMinutes = 60;   // Default time slot for Equipment / Sport room reservations
        this.groupId = -1L;    // If not a group reservation
    }

    /**
     * Instantiates a new Reservation.
     *
     * @param typeOfReservation       the type of reservation
     * @param bookedItemName          the booked item name
     * @param customerId              the customer id
     * @param sportFacilityReservedId the sport facility reserved id
     * @param startingTime            the starting time
     * @param groupId                 the group id
     * @param madeByPremiumUser       if its made by a premium user
     */
    public Reservation(ReservationType typeOfReservation, String bookedItemName, Long customerId,
                       Long sportFacilityReservedId, LocalDateTime startingTime, Long groupId,
                       Boolean madeByPremiumUser) {

        this.typeOfReservation = typeOfReservation;
        this.bookedItemName = bookedItemName;
        this.customerId = customerId;
        this.sportFacilityReservedId = sportFacilityReservedId;
        this.startingTime = startingTime;
        this.madeByPremiumUser = madeByPremiumUser;
        this.timeSlotInMinutes = 60;
        this.groupId = groupId;
    }

    /**
     * Instantiates a new Reservation.
     */
    public Reservation() {
    }

    /**
     * Gets booked item name.
     *
     * @return the booked item name
     */
    public String getBookedItemName() {
        return bookedItemName;
    }

    /**
     * Sets booked item name.
     *
     * @param bookedItemName the booked item name
     */
    public void setBookedItemName(String bookedItemName) {
        this.bookedItemName = bookedItemName;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return reservationId;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.reservationId = id;
    }

    /**
     * Gets sport facility reserved id.
     *
     * @return the sport facility reserved id
     */
    public Long getSportFacilityReservedId() {
        return sportFacilityReservedId;
    }

    /**
     * Sets sport facility reserved id.
     *
     * @param sportFacilityReservedId the sport facility reserved id
     */
    public void setSportFacilityReservedId(Long sportFacilityReservedId) {
        this.sportFacilityReservedId = sportFacilityReservedId;
    }

    /**
     * Gets type of reservation.
     *
     * @return the type of reservation
     */
    public ReservationType getTypeOfReservation() {
        return typeOfReservation;
    }

    /**
     * Sets type of reservation.
     *
     * @param typeOfReservation the type of reservation
     */
    public void setTypeOfReservation(ReservationType typeOfReservation) {
        this.typeOfReservation = typeOfReservation;
    }

    /**
     * Gets reservation id.
     *
     * @return the reservation id
     */
    public Long getReservationId() {
        return reservationId;
    }

    /**
     * Sets reservation id.
     *
     * @param reservationId the reservation id
     */
    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    /**
     * Gets starting time.
     *
     * @return the starting time
     */
    public LocalDateTime getStartingTime() {
        return startingTime;
    }

    /**
     * Sets starting time.
     *
     * @param startingTime the starting time
     */
    public void setStartingTime(LocalDateTime startingTime) {
        this.startingTime = startingTime;
    }

    /**
     * Gets customer id.
     *
     * @return the customer id
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * Sets customer id.
     *
     * @param customerId the customer id
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * Gets time slot in minutes.
     *
     * @return the time slot in minutes
     */
    public int getTimeSlotInMinutes() {
        return timeSlotInMinutes;
    }

    /**
     * Sets time slot in minutes.
     *
     * @param timeSlotInMinutes the time slot in minutes
     */
    public void setTimeSlotInMinutes(int timeSlotInMinutes) {
        this.timeSlotInMinutes = timeSlotInMinutes;
    }

    /**
     * Gets group id.
     *
     * @return the group id
     */
    public Long getGroupId() {
        return groupId;
    }

    /**
     * Sets group id.
     *
     * @param groupId the group id
     */
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
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

    public Boolean getMadeByPremiumUser() {
        return madeByPremiumUser;
    }

    public void setMadeByPremiumUser(Boolean madeByPremiumUser) {
        this.madeByPremiumUser = madeByPremiumUser;
    }
}
