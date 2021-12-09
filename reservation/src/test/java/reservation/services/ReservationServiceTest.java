package reservation.services;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reservation.entities.Reservation;
import reservation.entities.ReservationType;
import reservation.repositories.ReservationRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private transient ReservationRepository reservationRepository;

    private transient ReservationService reservationService;

    private transient Reservation reservation1;
    private transient Reservation reservation2;


    /**
     * runs before each test
     */
    @BeforeEach
    void setup() {
        reservationService = new ReservationService(reservationRepository);
        reservation1 =
            new Reservation(ReservationType.EQUIPMENT, 1L, 42L,
                LocalDateTime.of(2022,10,05,16,00));
        reservation2 =
            new Reservation(ReservationType.SPORTS_FACILITY ,2L, 25L,
                LocalDateTime.of(2022,10,05,17,45));
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

        assertThat(reservationService.getUserReservationCountOnDay(start, end, 1L))
            .isEqualTo(1);
        verify(reservationRepository,
            times(1)).findReservationByStartingTimeBetweenAndCustomerId(start, end
            , 1L);
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
        reservation.setCustomerId(1l);
        reservation.setSportFacilityReservedId(2l);
        LocalDateTime l = LocalDateTime.of(2020,01,01,01,01);
        reservation.setStartingTime(l);

        when(reservationRepository.findById(1l)).thenReturn(reservation);

        assertThat(reservationService.getReservation(1l)).isNotNull();
        assertThat(reservationService.getReservation(1l)).isEqualTo(reservation);

        verify(reservationRepository, times(2)).findById(1l);
    }

    @Test
    void deleteReservationTest() {
        Reservation reservation = new Reservation();
        reservation.setTypeOfReservation(ReservationType.EQUIPMENT);
        reservation.setCustomerId(1l);
        reservation.setSportFacilityReservedId(2l);
        LocalDateTime l = LocalDateTime.of(2020,01,01,01,01);
        reservation.setStartingTime(l);

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
        reservation.setCustomerId(1l);
        reservation.setSportFacilityReservedId(2l);
        LocalDateTime l = LocalDateTime.of(2020,01,01,01,01);
        reservation.setStartingTime(l);

        when(reservationRepository.save(reservation)).thenReturn(reservation);

        assertThat(reservationService.makeSportFacilityReservation(reservation)).isEqualTo(reservation);
        verify(reservationRepository, times(1)).save(reservation);

    }


    @Test
    @Disabled
    void restTemplateTest() {

    }


}
