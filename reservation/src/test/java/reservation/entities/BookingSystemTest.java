package reservation.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;
import reservation.entities.strategy.BasicPremiumUserStrategy;
import reservation.entities.strategy.BookingSystem;
import reservation.entities.strategy.ChronologicalStrategy;
import reservation.entities.strategy.EquipmentNameStrategy;
import reservation.entities.strategy.ReservationSortingStrategy;
import reservation.entities.strategy.UserIdStrategy;
import reservation.services.ReservationService;

public class BookingSystemTest {

    transient List<Reservation> userIdStrategy = new ArrayList<>();
    transient BookingSystem bookingSystem = new BookingSystem(new ChronologicalStrategy());
    transient Reservation[] reservations;

    @BeforeEach
    void setup() {
        int size = 6;
        reservations = new Reservation[size];

        String[] titles = {"Tango", "Krav Maga", "Ziou Zitsou", "Krav Maga", "Box", "Yoga"};

        for (int i = 0; i < size; i++) {
            Reservation reservation =
                new Reservation(ReservationType.EQUIPMENT, titles[i], (long) i, (long) i,
                    LocalDateTime.of(2020, i + 1, 1, 1, 1), false);
            reservation.setReservationId((long) i + 1);
            userIdStrategy.add(reservation);
            reservations[i] = reservation;
        }
    }

    @Test
    void constructorTest() {
        assertNotNull(bookingSystem);
    }

    @Test
    void addReservation() {
        bookingSystem.addReservation(reservations[0]);
        assertEquals(reservations[0], bookingSystem.getNextReservation());
    }

    @Test
    void getNextReservationChronologically() {
        BookingSystem chronologicalStrategy = new BookingSystem(new ChronologicalStrategy());
        reservations[1].setStartingTime(reservations[0].getStartingTime());
        reservations[2].setStartingTime(LocalDateTime.of(2020, 1, 1, 0, 1));
        for (Reservation r : reservations) {
            chronologicalStrategy.addReservation(r);
        }

        assertEquals(reservations[5], chronologicalStrategy.getNextReservation());
    }

    @Test
    void getNextReservationChronologicallyEmpty() {
        BookingSystem chronologicalStrategy = new BookingSystem(new ChronologicalStrategy());

        assertNull(chronologicalStrategy.getNextReservation());
    }

    @Test
    void getNextReservationBasicPremium() {
        BookingSystem userPremiumStrategy =
            new BookingSystem(new BasicPremiumUserStrategy());

        for (int i = 0; i < 5; i++) {
            userPremiumStrategy.addReservation(reservations[i]);

            //only first and second user is premium
            if (i == 1 || i == 2) {
                reservations[i].setMadeByPremiumUser(true);
            }
        }

        assertEquals(reservations[1], userPremiumStrategy.getNextReservation());
    }

    @Test
    void getNextReservationBasicPremiumEmpty() {
        BookingSystem userPremiumStrategy =
            new BookingSystem(new BasicPremiumUserStrategy());

        assertNull(userPremiumStrategy.getNextReservation());
    }

    @Test
    void getNextReservationEquipmentName() {
        BookingSystem equipmentNameStrategy = new BookingSystem(new EquipmentNameStrategy());

        for (int i = 0; i < 6; i++) {
            equipmentNameStrategy.addReservation(reservations[i]);
        }

        assertEquals(reservations[4], equipmentNameStrategy.getNextReservation());
    }

    @Test
    void getNextReservationEquipmentNameEmpty() {
        BookingSystem equipmentNameStrategy = new BookingSystem(new EquipmentNameStrategy());

        assertNull(equipmentNameStrategy.getNextReservation());
    }

    @Test
    void getNextReservationEquipmentNameNull() {
        BookingSystem equipmentNameStrategy = new BookingSystem(new EquipmentNameStrategy());
        equipmentNameStrategy.addReservation(null);
        assertNull(equipmentNameStrategy.getNextReservation());
    }


    @Test
    void getNextReservationEquipmentNameWithDifferentObjects() {
        BookingSystem equipmentNameStrategy =
            new BookingSystem(new EquipmentNameStrategy());

        for (int i = 0; i < 6; i++) {
            equipmentNameStrategy.addReservation(reservations[i]);
        }
        reservations[0].setTypeOfReservation(ReservationType.LESSON);
        reservations[2].setTypeOfReservation(ReservationType.LESSON);
        reservations[3].setTypeOfReservation(ReservationType.LESSON);
        reservations[5].setTypeOfReservation(ReservationType.LESSON);

        assertEquals(reservations[4], equipmentNameStrategy.getNextReservation());
    }

    @Test
    void getNextReservationEquipmentNameWithDifferentObjects2() {
        BookingSystem equipmentNameStrategy =
            new BookingSystem(new EquipmentNameStrategy());

        for (int i = 0; i < 4; i++) {
            equipmentNameStrategy.addReservation(reservations[i]);
        }
        reservations[0].setTypeOfReservation(ReservationType.LESSON);
        reservations[1].setTypeOfReservation(ReservationType.EQUIPMENT);
        reservations[2].setTypeOfReservation(ReservationType.EQUIPMENT);
        reservations[3].setTypeOfReservation(ReservationType.LESSON);

        assertEquals(reservations[1], equipmentNameStrategy.getNextReservation());
    }

    @Test
    void getNextReservationUserId() {
        BookingSystem userIdStrategy = new BookingSystem(new UserIdStrategy());
        for (int i = 0; i < 4; i++) {
            userIdStrategy.addReservation(reservations[i]);
        }

        assertEquals(reservations[0], userIdStrategy.getNextReservation());
    }

    @Test
    void getNextReservationUserIdEmpty() {
        BookingSystem userIdStrategy = new BookingSystem(new UserIdStrategy());
        assertNull(userIdStrategy.getNextReservation());
    }

    @Test
    void toStringTest() {
        for (int i = 0; i < 3; i++) {
            bookingSystem.addReservation(reservations[i]);

        }
        assertEquals("BookingSystem{bookings=[" + reservations[0].toString() + ", "
                + reservations[1].toString() + ", " + reservations[2].toString() + "]}",
            bookingSystem.toString());
    }
}
