package reservation.chainofresponsibility;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
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
    LocalDateTime startDay;
    LocalDateTime endDay;

    private transient ReservationController reservationController;
    private transient ReservationService reservationService;
    // Class under test:
    private transient UserReservationBalanceValidator userReservationBalanceValidator;


    /**
     * Constructor for this test suite.
     */
    public UserReservationBalanceValidatorTest() {
        reservationService = mock(ReservationService.class);
        reservationController = mock(ReservationController.class);
        this.userReservationBalanceValidator =
            new UserReservationBalanceValidator(reservationService, reservationController);

        reservation1 = new Reservation(ReservationType.EQUIPMENT, 1L, 42L,
            LocalDateTime.of(2022, 10, 05, 16, 00));
        reservation1.setId(53L);

        startDay = LocalDateTime
            .parse(reservation1.getStartingTime().toString().substring(0, 10) + "T00:00:00");
        endDay = LocalDateTime
            .parse(reservation1.getStartingTime().toString().substring(0, 10) + "T23:59:59");
    }



    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        assertNotNull(userReservationBalanceValidator);
    }



    @Test
    public void testBasicUserLimitNotReachedYet() throws InvalidReservationException {

        when(reservationService.getUserReservationCountOnDay(startDay, endDay,
            1L)).thenReturn(0);
        when(reservationController.getUserIsPremium(anyLong())).thenReturn(false);

        this.userReservationBalanceValidator.handle(reservation1);

        verify(reservationService).getUserReservationCountOnDay(startDay, endDay, 1L);
        verify(reservationController).getUserIsPremium(1L);
    }



    @Test
    public void testBasicUserLimitReached() throws InvalidReservationException {

        when(reservationService.getUserReservationCountOnDay(startDay, endDay,
            1L)).thenReturn(1);
        when(reservationController.getUserIsPremium(anyLong())).thenReturn(false);

        assertThrows(InvalidReservationException.class, () -> {
            userReservationBalanceValidator.handle(reservation1);
        });
        verify(reservationService).getUserReservationCountOnDay(startDay, endDay, 1L);
        verify(reservationController).getUserIsPremium(1L);
    }



    @Test
    public void testPremiumUserLimitNotReachedYet() throws InvalidReservationException {

        when(reservationService.getUserReservationCountOnDay(startDay, endDay,
            1L)).thenReturn(2);
        when(reservationController.getUserIsPremium(anyLong())).thenReturn(true);

        this.userReservationBalanceValidator.handle(reservation1);

        verify(reservationService).getUserReservationCountOnDay(startDay, endDay, 1L);
        verify(reservationController).getUserIsPremium(1L);
    }


    @Test
    public void testPremiumUserLimitReached() throws InvalidReservationException {

        when(reservationService.getUserReservationCountOnDay(startDay, endDay,
            1L)).thenReturn(3);
        when(reservationController.getUserIsPremium(anyLong())).thenReturn(true);

        assertThrows(InvalidReservationException.class, () -> {
            userReservationBalanceValidator.handle(reservation1);
        });
        verify(reservationService).getUserReservationCountOnDay(startDay, endDay, 1L);
        verify(reservationController).getUserIsPremium(1L);
    }




}
