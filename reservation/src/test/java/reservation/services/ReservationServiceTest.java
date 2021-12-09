package reservation.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reservation.entities.Reservation;
import reservation.entities.ReservationType;
import reservation.repositories.ReservationRepository;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private transient ReservationRepository reservationRepository;

    private transient ReservationService reservationService;

    private transient Reservation reservation1;
    private transient Reservation reservation2;

    /**
     * runs before each test.
     */
    @BeforeEach
    void setup() {
        reservationService = new ReservationService(reservationRepository);
        reservation1 = new Reservation(ReservationType.EQUIPMENT, 1L, 42L,
            LocalDateTime.of(2022, 10, 05, 16, 00));
        reservation2 = new Reservation(ReservationType.SPORTS_FACILITY, 2L, 25L,
            LocalDateTime.of(2022, 10, 05, 17, 45));
    }

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

    //
    //    @Test
    //    void test() {
    //        String date = "2022-10-05T15:15:30";
    //        LocalDateTime start = LocalDateTime.parse(date.substring(0, 10) + "T00:00:00");
    //        LocalDateTime end = LocalDateTime.parse(date.substring(0, 10) + "T23:59:59");
    //    }

    @Test
    void getReservationTest() {

        Reservation reservation = new Reservation();
        reservation.setTypeOfReservation(ReservationType.EQUIPMENT);
        reservation.setCustomerId(1L);
        reservation.setSportFacilityReservedId(2L);
        LocalDateTime date = LocalDateTime.of(2020, 01, 01, 01, 01);
        reservation.setStartingTime(date);

        when(reservationRepository.findById(1L)).thenReturn(reservation);

        assertThat(reservationService.getReservation(1L)).isNotNull();
        assertThat(reservationService.getReservation(1L)).isEqualTo(reservation);

        verify(reservationRepository, times(2)).findById(1L);
    }

    @Test
    void deleteReservationTest() {
        Reservation reservation = new Reservation();
        reservation.setTypeOfReservation(ReservationType.EQUIPMENT);
        reservation.setCustomerId(1L);
        reservation.setSportFacilityReservedId(2L);
        LocalDateTime date = LocalDateTime.of(2020, 01, 01, 01, 01);
        reservation.setStartingTime(date);

        reservationService.deleteReservation(reservation.getReservationId());
        verify(reservationRepository, times(1)).deleteById(reservation.getReservationId());

    }

    @Test
    @Disabled
    void isAvailableTest() {
        //        Reservation reservation = new Reservation();
        //        reservation.setTypeOfReservation(ReservationType.EQUIPMENT);
        //        reservation.setCustomerId(1l);
        //        reservation.setSportFacilityReservedId(2l);
        //        LocalDateTime l = LocalDateTime.of(2020,01,01,01,01);
        //        reservation.setStartingTime(l);
        //
        //        Reservation reservation1 = new Reservation();
        //        reservation.setTypeOfReservation(ReservationType.EQUIPMENT);
        //        reservation.setCustomerId(2l);
        //        reservation.setSportFacilityReservedId(1l);
        //        LocalDateTime l1 = LocalDateTime.of(2020,01,01,01,01);
        //        reservation.setStartingTime(l1);

    }

    @Test
    void makeSportFacilityReservationTest() {
        Reservation reservation = new Reservation();
        reservation.setTypeOfReservation(ReservationType.EQUIPMENT);
        reservation.setCustomerId(1L);
        reservation.setSportFacilityReservedId(2L);
        LocalDateTime date = LocalDateTime.of(2020, 01, 01, 01, 01);
        reservation.setStartingTime(date);

        when(reservationRepository.save(reservation)).thenReturn(reservation);

        assertThat(reservationService.makeSportFacilityReservation(reservation))
            .isEqualTo(reservation);
        verify(reservationRepository, times(1)).save(reservation);

    }

    @Test
    @Disabled
    void restTemplateTest() {

    }

}
