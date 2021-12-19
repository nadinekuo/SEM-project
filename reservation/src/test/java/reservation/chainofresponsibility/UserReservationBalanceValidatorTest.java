package reservation.chainofresponsibility;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import reservation.controllers.ReservationController;
import reservation.entities.Reservation;
import reservation.entities.ReservationType;
import reservation.entities.chainofresponsibility.InvalidReservationException;
import reservation.entities.chainofresponsibility.UserReservationBalanceValidator;
import reservation.services.ReservationService;


@RunWith(MockitoJUnitRunner.class)
public class UserReservationBalanceValidatorTest {

    private transient Reservation reservation1;
    private transient Reservation reservation2;
    private transient Reservation groupReservation1;

    @Mock
    private transient ReservationController reservationController;
    @Mock
    private transient ReservationService reservationService;
    private transient UserReservationBalanceValidator userReservationBalanceValidator;


    public UserReservationBalanceValidatorTest() {
        reservation1 = new Reservation(ReservationType.EQUIPMENT, 1L, 42L,
            LocalDateTime.of(2022, 10, 05, 16, 00));
        reservation1.setId(53L);
        reservation2 = new Reservation(ReservationType.SPORTS_ROOM, 2L, 25L,
            LocalDateTime.of(2022, 10, 05, 17, 45));
        reservation2.setId(84L);
        groupReservation1 = new Reservation(ReservationType.SPORTS_ROOM, 3L, 13L,
            LocalDateTime.of(2022, 02, 3, 20, 30), 84L);
        groupReservation1.setId(99L);
    }

    @BeforeEach
    void setUp() {
//        reservationService = mock(ReservationService.class);
//        reservationController = mock(ReservationController.class);
        userReservationBalanceValidator =
            new UserReservationBalanceValidator(reservationService, reservationController);
    }

    @Test
    public void testBasicUserLimitReached() throws InvalidReservationException {

        LocalDateTime startDay = LocalDateTime
            .parse(reservation1.getStartingTime().toString().substring(0, 10) + "T00:00:00");
        LocalDateTime endDay = LocalDateTime
            .parse(reservation1.getStartingTime().toString().substring(0, 10) + "T23:59:59");

        when(reservationService.getUserReservationCountOnDay(startDay, endDay,
            1L)).thenReturn(1);
        when(reservationController.getUserIsPremium(1L)).thenReturn(false);
//
//        assertThrows(InvalidReservationException.class, () -> {
//            userReservationBalanceValidator.handle(reservation1);
//        });
        userReservationBalanceValidator.handle(reservation1);

        verify(reservationService).getUserReservationCountOnDay(startDay, endDay, 1L);
        verify(reservationController).getUserIsPremium(1L);
    }



}
