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

    private final transient long id1 = 53L;
    private final transient long id2 = 84L;
    private final transient long idNotExists = 42L;
    private final transient long idGroup = 99L;

    private final transient LocalDateTime date1;
    private final transient LocalDateTime date2;

    @Mock
    private transient ReservationRepository reservationRepository;

    @Mock
    private transient UserReservationBalanceValidator userReservationBalanceValidator;

    private transient ReservationService reservationService;

    /**
     * Instantiates a new Reservation service test.
     */
    public ReservationServiceTest() {
        date1 = LocalDateTime.of(2022, 10, 5, 16, 0);
        date2 = LocalDateTime.of(2022, 10, 5, 17, 45);
        reservation1 = new Reservation(ReservationType.EQUIPMENT, 1L, 42L,
            date1);
        reservation1.setId(id1);
        reservation2 = new Reservation(ReservationType.SPORTS_ROOM, 2L, 25L,
            date2);
        reservation2.setId(id2);
        groupReservation1 = new Reservation(ReservationType.SPORTS_ROOM, 3L, 13L,
            date2, id2);
        groupReservation1.setId(idGroup);
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

        when(reservationRepository.findById(id1)).thenReturn(Optional.of(reservation1));

        Reservation result = reservationService.getReservation(id1);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id1);
        assertThat(result.getTypeOfReservation() == ReservationType.EQUIPMENT);
        verify(reservationRepository, times(1)).findById(id1);
    }

    /**
     * Delete reservation test.
     */
    @Test
    void deleteReservationTest() {

        when(reservationRepository.findById(id1)).thenReturn(Optional.ofNullable(reservation1));

        assertDoesNotThrow(() -> {
            reservationService.deleteReservation(id1);
        });

        verify(reservationRepository, times(1)).deleteById(id1);
    }

    /**
     * Delete non existing reservation test.
     */
    @Test
    void deleteNonExistingReservationTest() {

        when(reservationRepository.findById(idNotExists)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            reservationService.deleteReservation(idNotExists);
        });
        verify(reservationRepository, never()).deleteById(any());
    }

    /**
     * Valid reservation passed through chain of responsibility.
     * Validator is mocked, since their logic is tested in the Validator unit tests.
     */
    @Test
    void checkValidReservationTest() throws InvalidReservationException {

        ReservationService reservationServiceSpy =
            Mockito.spy(new ReservationService(reservationRepository));
        Mockito.doReturn(userReservationBalanceValidator)
            .when(reservationServiceSpy).createChainOfResponsibility(any(), any());

        when(userReservationBalanceValidator.handle(reservation1)).thenReturn(true);

        assertTrue(reservationServiceSpy.checkReservation(reservation1,
            new ReservationController(reservationServiceSpy)));
    }

    /**
     * Invalid reservation passed through chain of responsibility.
     * Validator is mocked, since their logic is tested in the Validator unit tests.
     */
    @Test
    void checkInvalidReservationTest() throws InvalidReservationException {

        ReservationService reservationServiceSpy =
            Mockito.spy(new ReservationService(reservationRepository));
        Mockito.doReturn(userReservationBalanceValidator)
            .when(reservationServiceSpy).createChainOfResponsibility(any(), any());

        when(userReservationBalanceValidator.handle(reservation1)).thenReturn(false);

        assertFalse(reservationServiceSpy.checkReservation(reservation1,
            new ReservationController(reservationServiceSpy)));
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

        when(
            reservationRepository.findReservationByStartingTimeBetweenAndCustomerId(start, end, 1L))
            .thenReturn(List.of(reservation1, reservation2));

        assertThat(reservationService.getUserReservationCountOnDay(start, end, 1L)).isEqualTo(1);
        verify(reservationRepository, times(1))
            .findReservationByStartingTimeBetweenAndCustomerId(start, end, 1L);
    }

    /**
     * Available sport facility.
     */
    @Test
    void availableSportFacility() {

        when(reservationRepository.findBySportFacilityReservedIdAndTime(anyLong(), any()))
            .thenReturn(Optional.empty());   // Facility is unoccupied

        assertThat(reservationService
            .sportsFacilityIsAvailable(id1, date1)).isTrue();

        verify(reservationRepository, times(1))
            .findBySportFacilityReservedIdAndTime(id1, date1);
    }

    /**
     * Unavailable sport facility.
     */
    @Test
    void unavailableSportFacility() {

        when(reservationRepository.findBySportFacilityReservedIdAndTime(anyLong(), any()))
            .thenReturn(Optional.of(id1));   // Facility is reserved for this time already!

        assertThat(reservationService
            .sportsFacilityIsAvailable(id1, date1)).isFalse();

        verify(reservationRepository, times(1))
            .findBySportFacilityReservedIdAndTime(id1, date1);
    }

    /**
     * Make sport facility reservation test.
     */
    @Test
    void makeSportFacilityReservationTest() {

        when(reservationRepository.save(reservation1)).thenReturn(reservation1);

        Reservation result = reservationService.makeSportFacilityReservation(reservation1);
        assertThat(result.getId()).isEqualTo(id1);
        assertThat(result.getStartingTime()).isEqualTo(date1);

        verify(reservationRepository, times(1)).save(reservation1);

    }

    /**
     * Gets last person that used equipment test.
     */
    @Test
    void getLastPersonThatUsedEquipmentTest() {
        List<Reservation> reservations = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Reservation r = new Reservation(ReservationType.EQUIPMENT, (long) i, 2L,
                LocalDateTime.of(2022, i + 1, 1, 0, 0), 1L);
            r.setId((long) i);
            reservations.add(r);
        }

        when(reservationRepository.findReservationsBySportFacilityReservedId(2L))
            .thenReturn(reservations);
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
