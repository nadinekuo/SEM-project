package reservation.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import reservation.controllers.ReservationController;
import reservation.entities.Reservation;
import reservation.entities.ReservationType;
import reservation.entities.chainofresponsibility.InvalidReservationException;
import reservation.entities.chainofresponsibility.UserReservationBalanceValidator;
import reservation.repositories.ReservationRepository;

/**
 * The type Reservation service test.
 */
@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    private final transient Reservation reservation1;
    private final transient Reservation reservation2;
    private final transient Reservation groupReservation1;

    @Mock
    private transient ReservationRepository reservationRepository;

    @Mock
    private transient UserReservationBalanceValidator userReservationBalanceValidator;

    private transient ReservationService reservationService;
    private static boolean madeByPremiumUser = true;

    /**
     * Instantiates a new Reservation service test.
     */
    public ReservationServiceTest() {
        reservation1 = new Reservation(ReservationType.EQUIPMENT, "hockey", 1L, 42L,
            LocalDateTime.of(2022, 10, 05, 16, 00), madeByPremiumUser);
        reservation1.setId(53L);
        reservation2 = new Reservation(ReservationType.SPORTS_ROOM, "hockey", 2L, 25L,
            LocalDateTime.of(2022, 10, 05, 17, 45), madeByPremiumUser);
        reservation2.setId(84L);
        groupReservation1 = new Reservation(ReservationType.SPORTS_ROOM, "Hall 1", 3L, 13L,
            LocalDateTime.of(2022, 02, 3, 20, 30), 84L, madeByPremiumUser);
        groupReservation1.setId(99L);
    }

    /**
     * Sets test attributes.
     */
    @BeforeEach
    void setup() {
        reservationService = new ReservationService(reservationRepository);
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        assertNotNull(reservationService);
    }

    /**
     * Gets reservation test.
     */
    @Test
    void getReservationTest() {

        when(reservationRepository.findById(53L)).thenReturn(Optional.of(reservation1));

        Reservation result = reservationService.getReservation(53L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(53L);
        assertThat(result.getTypeOfReservation() == ReservationType.EQUIPMENT);
        verify(reservationRepository, times(1)).findById(53L);
    }

    /**
     * Delete reservation test.
     */
    @Test
    void deleteReservationTest() {

        when(reservationRepository.existsById(53L)).thenReturn(true);

        assertDoesNotThrow(() -> {
            reservationService.deleteReservation(53L);
        });

        verify(reservationRepository, times(1)).deleteById(53L);
    }

    /**
     * Delete non existing reservation test.
     */
    @Test
    void deleteNonExistingReservationTest() {

        when(reservationRepository.existsById(53L)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> {
            reservationService.deleteReservation(53L);
        });
        verify(reservationRepository, never()).deleteById(any());
    }

    /**
     * Count one sport facility reservation test.
     */
    @Test
    void countOneSportFacilityReservationTest() {

        String date = "2022-10-05T15:00:30";
        LocalDateTime start = LocalDateTime.parse(date.substring(0, 10) + "T00:00:00");
        LocalDateTime end = LocalDateTime.parse(date.substring(0, 10) + "T23:59:59");

        //        LocalDateTime start = LocalDateTime.of(2022, 10, 05, 00, 00, 00);
        //        LocalDateTime end = LocalDateTime.of(2022, 10, 05, 23, 59, 59);

        when(reservationRepository.findReservationByStartingTimeBetweenAndCustomerId(start, end,
            1L)).thenReturn(List.of(reservation1, reservation2));

        assertThat(reservationService.getUserReservationCountOnDay(start, end, 1L)).isEqualTo(1);
        verify(reservationRepository, times(1)).findReservationByStartingTimeBetweenAndCustomerId(
            start, end, 1L);
    }

    /**
     * Available sport facility.
     */
    @Test
    void availableSportFacility() {

        when(reservationRepository.findBySportFacilityReservedIdAndTime(anyLong(),
            any())).thenReturn(Optional.empty());   // Facility is unoccupied

        assertThat(reservationService.sportsFacilityIsAvailable(75L,
            LocalDateTime.of(2022, 10, 05, 16, 00))).isTrue();

        verify(reservationRepository, times(1)).findBySportFacilityReservedIdAndTime(75L,
            LocalDateTime.of(2022, 10, 05, 16, 00));
    }

    /**
     * Unavailable sport facility.
     */
    @Test
    void unavailableSportFacility() {

        when(reservationRepository.findBySportFacilityReservedIdAndTime(anyLong(),
            any())).thenReturn(Optional.of(75L));   // Facility is reserved for this time already!

        assertThat(reservationService.sportsFacilityIsAvailable(75L,
            LocalDateTime.of(2022, 10, 05, 16, 00))).isFalse();

        verify(reservationRepository, times(1)).findBySportFacilityReservedIdAndTime(75L,
            LocalDateTime.of(2022, 10, 05, 16, 00));
    }

    /**
     * Make sport facility reservation test.
     */
    @Test
    void makeSportFacilityReservationTest() {

        when(reservationRepository.save(reservation1)).thenReturn(reservation1);

        Reservation result = reservationService.makeSportFacilityReservation(reservation1);
        assertThat(result.getId()).isEqualTo(53L);
        assertThat(result.getStartingTime()).isEqualTo(LocalDateTime.of(2022, 10, 05, 16, 00));

        verify(reservationRepository, times(1)).save(reservation1);

    }

    /**
     * Gets last person that used equipment test.
     */
    @Test
    void getLastPersonThatUsedEquipmentTest() {
        List<Reservation> reservations = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Reservation r = new Reservation(ReservationType.EQUIPMENT, "hockey", (long) i, 2L,
                LocalDateTime.of(2022, i + 1, 1, 0, 0), 1L, madeByPremiumUser);
            r.setId((long) i);
            reservations.add(r);
        }

        when(reservationRepository.findReservationsBySportFacilityReservedId(2L)).thenReturn(
            reservations);
        assertEquals(Optional.of(4L),
            Optional.of(reservationService.getLastPersonThatUsedEquipment(2L)));

    }

    /**
     * Rest template test.
     */
    @Test
    public void restTemplateTest() {
        RestTemplate restTemplate = reservationService.restTemplate();
        assertNotNull(restTemplate);
    }

}
