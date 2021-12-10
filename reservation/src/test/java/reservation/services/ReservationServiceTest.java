package reservation.services;

import static org.assertj.core.api.Assertions.assertThat;
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
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
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
    private transient Reservation groupReservation1;

    /**
     * runs before each test.
     */
    @BeforeEach
    void setup() {
        reservationService = new ReservationService(reservationRepository);
    }

    public ReservationServiceTest() {
        reservation1 = new Reservation(53L, ReservationType.EQUIPMENT, 1L, 42L,
            LocalDateTime.of(2022, 10, 05, 16, 00));
        reservation2 = new Reservation(84L, ReservationType.SPORTS_FACILITY, 2L, 25L,
            LocalDateTime.of(2022, 10, 05, 17, 45));
        groupReservation1 = new Reservation(99L, ReservationType.SPORTS_FACILITY, 3L, 13L,
            LocalDateTime.of(2022, 02, 3, 20, 30), 84L);
    }


    @Test
    public void testConstructor() {
        assertNotNull(reservationService);
    }


    @Test
    void getReservationTest() {

        when(reservationRepository.findById(53L)).thenReturn(Optional.of(reservation1));

        Reservation result = reservationService.getReservation(53L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(53L);
        assertThat(result.getTypeOfReservation() == ReservationType.EQUIPMENT);
        verify(reservationRepository, times(1)).findById(53L);
    }


    @Test
    void deleteReservationTest() {

        when(reservationRepository.existsById(53L)).thenReturn(true);

        assertDoesNotThrow(() -> {
            reservationService.deleteReservation(53L);
        });

        verify(reservationRepository, times(1)).deleteById(53L);
    }

    @Test
    void deleteNonExistingReservationTest() {

        when(reservationRepository.existsById(53L)).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> {
            reservationService.deleteReservation(53L);
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

        when(
            reservationRepository.findReservationByStartingTimeBetweenAndCustomerId(start, end, 1L))
            .thenReturn(List.of(reservation1, reservation2));

        assertThat(reservationService.getUserReservationCountOnDay(start, end, 1L)).isEqualTo(1);
        verify(reservationRepository, times(1))
            .findReservationByStartingTimeBetweenAndCustomerId(start, end, 1L);
    }

    @Test
    void availableSportFacility() {

        when(reservationRepository.findBySportFacilityReservedIdAndTime(anyLong(), any()))
            .thenReturn(Optional.empty());   // Facility is unoccupied

        assertThat(reservationService.sportsFacilityIsAvailable(75L,
            LocalDateTime.of(2022, 10, 05, 16, 00)))
            .isTrue();

        verify(reservationRepository, times(1))
            .findBySportFacilityReservedIdAndTime(75L,  LocalDateTime.of(2022, 10, 05, 16, 00));
    }


    @Test
    void unavailableSportFacility() {

        when(reservationRepository.findBySportFacilityReservedIdAndTime(anyLong(), any()))
            .thenReturn(Optional.of(75L));   // Facility is reserved for this time already!

        assertThat(reservationService.sportsFacilityIsAvailable(75L,
            LocalDateTime.of(2022, 10, 05, 16, 00)))
            .isFalse();

        verify(reservationRepository, times(1))
            .findBySportFacilityReservedIdAndTime(75L,  LocalDateTime.of(2022, 10, 05, 16, 00));
    }




    @Test
    void makeSportFacilityReservationTest() {

        when(reservationRepository.save(reservation1)).thenReturn(reservation1);

        Reservation result = reservationService.makeSportFacilityReservation(reservation1);
        assertThat(result.getId()).isEqualTo(53L);
        assertThat(result.getStartingTime()).isEqualTo( LocalDateTime.of(2022, 10, 05, 16, 00));

        verify(reservationRepository, times(1)).save(reservation1);

    }


    @Test
    public void restTemplateTest() {
        RestTemplate restTemplate = reservationService.restTemplate();
        assertNotNull(restTemplate);
    }

}
