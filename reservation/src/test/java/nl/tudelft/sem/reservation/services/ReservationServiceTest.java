package nl.tudelft.sem.reservation.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
import nl.tudelft.sem.reservation.entities.Reservation;
import nl.tudelft.sem.reservation.entities.ReservationType;
import nl.tudelft.sem.reservation.entities.chainofresponsibility.UserReservationBalanceValidator;
import nl.tudelft.sem.reservation.repositories.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    private static final boolean madeByPremiumUser = true;
    private final transient Reservation reservation1;
    private final transient Reservation reservation2;
    private final transient Reservation groupReservation1;
    private final transient long reservationId1;
    private final transient long reservationId2;
    private final transient long groupReservationId;
    LocalDateTime time1;
    @Mock
    private transient ReservationRepository reservationRepository;
    @Mock
    private transient UserReservationBalanceValidator userReservationBalanceValidator;
    private transient ReservationService reservationService;

    /**
     * Instantiates a new Reservation service test.
     */
    public ReservationServiceTest() {
        reservationId1 = 1L;
        reservationId2 = 2L;
        groupReservationId = 99L;
        time1 = LocalDateTime.of(2022, 10, 05, 16, 00);

        reservation1 =
            new Reservation(ReservationType.EQUIPMENT, "hockey", 1L, 42L, time1, madeByPremiumUser);
        reservation1.setId(reservationId1);
        reservation2 = new Reservation(ReservationType.SPORTS_ROOM, "hockey", 2L, 25L,
            LocalDateTime.of(2022, 10, 05, 17, 45), madeByPremiumUser);
        reservation2.setId(reservationId2);
        groupReservation1 = new Reservation(ReservationType.SPORTS_ROOM, "Hall 1", 3L, 13L,
            LocalDateTime.of(2022, 02, 3, 20, 30), 84L, madeByPremiumUser);
        groupReservation1.setId(groupReservationId);
    }

    /**
     * Sets up test attributes.
     */
    @BeforeEach
    void setup() {
        reservationService = new ReservationService(reservationRepository);
    }

    @Test
    public void constructorTest() {
        assertNotNull(reservationService);
    }

    @Test
    void getReservationTest() {
        when(reservationRepository.findById(reservationId1)).thenReturn(Optional.of(reservation1));

        Reservation result = reservationService.getReservation(reservationId1);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(reservationId1);
        assertThat(result.getTypeOfReservation() == ReservationType.EQUIPMENT);
        verify(reservationRepository, times(1)).findById(reservationId1);
    }

    @Test
    void getReservationThrowsExceptionTest() {
        when(reservationRepository.findById(reservationId1)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class,
            () -> reservationService.getReservation(reservationId1));

    }

    @Test
    void findByGroupIdAndTimeTest() {
        when(reservationRepository.findByGroupIdAndTime(groupReservationId, time1)).thenReturn(
            Optional.of(reservationId1));
        assertThat(reservationService.findByGroupIdAndTime(groupReservationId, time1)).isEqualTo(
            reservationId1);
    }

    @Test
    void findByGroupIdAndTimeNullTest() {
        when(reservationRepository.findByGroupIdAndTime(groupReservationId, time1)).thenReturn(
            Optional.empty());
        assertThat(reservationService.findByGroupIdAndTime(groupReservationId, time1)).isEqualTo(
            null);
    }

    @Test
    void deleteReservationTest() {

        when(reservationRepository.existsById(reservationId1)).thenReturn(true);

        assertDoesNotThrow(() -> {
            reservationService.deleteReservation(reservationId1);
        });

        verify(reservationRepository, times(1)).deleteById(reservationId1);
    }

    @Test
    void deleteNonExistingReservationTest() {

        when(reservationRepository.existsById(reservationId1)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> {
            reservationService.deleteReservation(reservationId1);
        });
        verify(reservationRepository, never()).deleteById(any());
    }

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

    @Test
    void availableSportFacilityTest() {

        when(reservationRepository.findBySportFacilityReservedIdAndTime(anyLong(),
            any())).thenReturn(Optional.empty());   // Facility is unoccupied

        assertThat(reservationService.sportsFacilityIsAvailable(75L,
            LocalDateTime.of(2022, 10, 05, 16, 00))).isTrue();

        verify(reservationRepository, times(1)).findBySportFacilityReservedIdAndTime(75L,
            LocalDateTime.of(2022, 10, 05, 16, 00));
    }

    @Test
    void unavailableSportFacilityTest() {

        when(reservationRepository.findBySportFacilityReservedIdAndTime(anyLong(),
            any())).thenReturn(Optional.of(75L));   // Facility is reserved for this time already!

        assertThat(reservationService.sportsFacilityIsAvailable(75L,
            LocalDateTime.of(2022, 10, 05, 16, 00))).isFalse();

        verify(reservationRepository, times(1)).findBySportFacilityReservedIdAndTime(75L,
            LocalDateTime.of(2022, 10, 05, 16, 00));
    }

    @Test
    void makeSportFacilityReservationTest() {

        when(reservationRepository.save(reservation1)).thenReturn(reservation1);

        Reservation result = reservationService.makeSportFacilityReservation(reservation1);
        assertThat(result.getId()).isEqualTo(reservationId1);
        assertThat(result.getStartingTime()).isEqualTo(LocalDateTime.of(2022, 10, 05, 16, 00));

        verify(reservationRepository, times(1)).save(reservation1);

    }

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

    @Test
    public void restTemplateTest() {
        RestTemplate restTemplate = reservationService.restTemplate();
        assertNotNull(restTemplate);
    }

}
