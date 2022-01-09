package reservation.entities;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.time.Month;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReservationTest {

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

    Reservation reservation;

    /**
     * Sets up the tests.
     *
     */
    @BeforeEach
    void setup() {
        typeOfReservation = ReservationType.EQUIPMENT;
        bookedItemName = "HockeyStick";
        customerId = 1L;
        sportFacilityReservedId = 1L;
        startingTime = LocalDateTime.of(2022, Month.JANUARY, 5, 17, 00);
        madeByPremiumUser = false;

        reservation = new Reservation(typeOfReservation, bookedItemName, customerId,
                sportFacilityReservedId, startingTime, madeByPremiumUser);
    }

    @Test
    void constructorTest() {
        assertNotNull(reservation);
    }

    @Test
    void getBookedItemNameTest() {
        assertTrue(reservation.getBookedItemName().equals("HockeyStick"));
    }

    @Test
    void setBookedItemNameTest() {
        reservation.setBookedItemName("HockeyBall");
        assertTrue(reservation.getBookedItemName().equals("HockeyBall"));
    }

    @Test
    void getSportFacilityReservedIdTest() {
        assertTrue(reservation.getSportFacilityReservedId().equals(1L));
    }

    @Test
    void setSportFacilityReservedIdTest() {
        reservation.setSportFacilityReservedId(2L);
        assertTrue(reservation.getSportFacilityReservedId().equals(2L));
    }

    @Test
    void setAndGetIdTest() {
        reservation.setId(2L);
        assertTrue(reservation.getId().equals(2L));
    }

    @Test
    void setAndGetReservationIdTest() {
        reservation.setReservationId(2L);
        assertTrue(reservation.getReservationId().equals(2L));
    }

    @Test
    void getTypeOfReservationTest() {
        assertTrue(reservation.getTypeOfReservation().equals(ReservationType.EQUIPMENT));
    }

    @Test
    void setTypeOfReservationTest() {
        reservation.setTypeOfReservation(ReservationType.LESSON);
        assertTrue(reservation.getTypeOfReservation().equals(ReservationType.LESSON));
    }

    @Test
    void getStartingTimeTest() {
        assertTrue(reservation.getStartingTime().equals(
                LocalDateTime.of(2022, Month.JANUARY, 5, 17, 00)));
    }

    @Test
    void setStartingTimeTest() {
        reservation.setStartingTime(LocalDateTime.of(2021, Month.JANUARY, 5, 17, 00));
        assertTrue(reservation.getStartingTime().equals(
                LocalDateTime.of(2021, Month.JANUARY, 5, 17, 00)));
    }

    @Test
    void getCustomerIdTest() {
        assertTrue(reservation.getCustomerId().equals(1L));
    }

    @Test
    void setCustomerIdTest() {
        reservation.setCustomerId(2L);
        assertTrue(reservation.getCustomerId().equals(2L));
    }

    @Test
    void getTimeSlotInMinutesTest() {
        assertEquals(60, reservation.getTimeSlotInMinutes());
    }

    @Test
    void setTimeSlotInMinutesTest() {
        reservation.setTimeSlotInMinutes(30);
        assertEquals(30, reservation.getTimeSlotInMinutes());
    }

    @Test
    void getGroupIdTest() {
        assertTrue(reservation.getGroupId().equals(-1L));
    }

    @Test
    void setGroupIdTest() {
        reservation.setGroupId(1L);
        assertTrue(reservation.getGroupId().equals(1L));
    }

    @Test
    void equalsTheSameTest() {
        assertTrue(reservation.equals(reservation));
    }

    @Test
    void equalsTrueTest() {
        typeOfReservation = ReservationType.EQUIPMENT;
        bookedItemName = "HockeyStick";
        customerId = 1L;
        sportFacilityReservedId = 1L;
        startingTime = LocalDateTime.of(2022, Month.JANUARY, 5, 17, 00);
        madeByPremiumUser = false;

        Reservation check = new Reservation(typeOfReservation, bookedItemName, customerId,
                sportFacilityReservedId, startingTime, madeByPremiumUser);
        check.setReservationId(2L);
        reservation.setReservationId(2L);
        assertTrue(reservation.equals(check));
    }

    @Test
    void equalsNullTest() {
        assertFalse(reservation.equals(null));
    }

    @Test
    void equalsDifferentClassTest() {
        assertFalse(reservation.equals("Not a reservation"));
    }

    @Test
    void notEqualTest() {
        typeOfReservation = ReservationType.EQUIPMENT;
        bookedItemName = "HockeyBall";
        customerId = 1L;
        sportFacilityReservedId = 2L;
        startingTime = LocalDateTime.of(2022, Month.JANUARY, 5, 17, 00);
        madeByPremiumUser = true;

        Reservation check = new Reservation(typeOfReservation, bookedItemName, customerId,
                sportFacilityReservedId, startingTime, madeByPremiumUser);
        check.setReservationId(2L);
        reservation.setReservationId(1L);
        assertFalse(reservation.equals(check));
    }

    @Test
    void toStringTest() {
        reservation.setReservationId(1L);
        assertEquals("Reservation{" + "reservationId="
                + reservation.getReservationId() + ", typeOfReservation="
                + reservation.getTypeOfReservation() + ", customerId="
                + reservation.getCustomerId() + ", groupId="
                + reservation.getGroupId() + ", startingTime="
                + reservation.getStartingTime() + '}', reservation.toString());
    }

    @Test
    void getMadeByPremiumUserTest() {
        assertFalse(reservation.getMadeByPremiumUser());
    }

    @Test
    void setMadeByPremiumUserTest() {
        reservation.setMadeByPremiumUser(true);
        assertTrue(reservation.getMadeByPremiumUser());
    }

}
