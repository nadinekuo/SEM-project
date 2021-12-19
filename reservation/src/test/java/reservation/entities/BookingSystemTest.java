package reservation.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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

    /**
     * The Reservation service.
     */
    @Mock
    transient ReservationService reservationService;
    /**
     * The Rest template.
     */
    @Mock
    transient RestTemplate restTemplate;

    ReservationSortingStrategy sortingStrategy = new ChronologicalStrategy();
    transient List<Reservation> userIdStrategy = new ArrayList<>();
    transient BookingSystem bookingSystem = new BookingSystem(new ChronologicalStrategy());
    transient String equipmentUrl = "http://eureka-equipment";
    transient String userUrl = "http://eureka-user";

    transient Reservation[] reservations;

    @BeforeEach
    void setup() {
        int size = 6;
        restTemplate = Mockito.mock(RestTemplate.class);
        reservations = new Reservation[size];

        for (int i = 0; i < size; i++) {
            Reservation reservation = new Reservation(ReservationType.EQUIPMENT, (long) i, (long) i,
                LocalDateTime.of(2020, i + 1, 1, 1, 1));
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
            new BookingSystem(new BasicPremiumUserStrategy(restTemplate));

        for (int i = 0; i < 4; i++) {
            userPremiumStrategy.addReservation(reservations[i]);

            //only third user is premium
            boolean premium = i == 1 || i == 2;

            Mockito.when(restTemplate
                .getForObject(userUrl + "/user/" + reservations[i].getCustomerId() + "/isPremium",
                    Boolean.class)).thenReturn(premium);
        }

        assertEquals(reservations[1], userPremiumStrategy.getNextReservation());
    }

    @Test
    void getNextReservationBasicPremiumEmpty() {
        BookingSystem userPremiumStrategy =
            new BookingSystem(new BasicPremiumUserStrategy(restTemplate));

        assertNull(userPremiumStrategy.getNextReservation());
    }

    @Test
    void getNextReservationEquipmentName() {
        BookingSystem equipmentNameStrategy =
            new BookingSystem(new EquipmentNameStrategy(restTemplate));

        for (int i = 0; i < 4; i++) {
            equipmentNameStrategy.addReservation(reservations[i]);
        }

        String[] titles = {"Tango", "Krav Maga", "Ziou Zitsou", "Krav Maga"};

        for (int i = 0; i < 4; i++) {
            Mockito.when(restTemplate.getForObject(
                equipmentUrl + "/equipment/" + reservations[i].getSportFacilityReservedId()
                    + "/getEquipmentName", String.class)).thenReturn(titles[i]);
        }

        assertEquals(reservations[1], equipmentNameStrategy.getNextReservation());
    }

    @Test
    void getNextReservationEquipmentNameEmpty() {
        BookingSystem equipmentNameStrategy =
            new BookingSystem(new EquipmentNameStrategy(restTemplate));

        assertNull(equipmentNameStrategy.getNextReservation());
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
        assertEquals(
            "BookingSystem{bookings=[" + reservations[0].toString() + ", " + reservations[1]
                .toString() + ", " + reservations[2].toString() + "]}", bookingSystem.toString());
    }
}
