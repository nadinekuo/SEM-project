package reservation.chainofresponsibility;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import reservation.controllers.ReservationController;
import reservation.entities.Reservation;
import reservation.entities.ReservationType;
import reservation.entities.chainofresponsibility.InvalidReservationException;
import reservation.entities.chainofresponsibility.UserReservationBalanceValidator;
import reservation.services.ReservationService;

@RunWith(MockitoJUnitRunner.class)
public class UserReservationBalanceValidatorTest {

    LocalDateTime startDay;
    LocalDateTime endDay;
    private final transient Reservation reservation1;
    private final transient ReservationController reservationController;
    private final transient ReservationService reservationService;
    // Class under test:
    private final transient UserReservationBalanceValidator userReservationBalanceValidator;


    /**
     * Constructor for this test suite.
     */
    public UserReservationBalanceValidatorTest() {
        reservationService = mock(ReservationService.class);
        reservationController = mock(ReservationController.class);
        this.userReservationBalanceValidator =
            new UserReservationBalanceValidator(reservationService, reservationController);

        reservation1 = new Reservation(ReservationType.EQUIPMENT, "hockey", 1L, 42L,
            LocalDateTime.of(2022, 10, 05, 16, 00), false);
        reservation1.setId(53L);

        startDay = LocalDateTime.parse(
            reservation1.getStartingTime().toString().substring(0, 10) + "T00:00:00");
        endDay = LocalDateTime.parse(
            reservation1.getStartingTime().toString().substring(0, 10) + "T23:59:59");
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

        when(reservationService.getUserReservationCountOnDay(startDay, endDay, 1L)).thenReturn(0);

        this.userReservationBalanceValidator.handle(reservation1);

        verify(reservationService).getUserReservationCountOnDay(startDay, endDay, 1L);
    }

    @Test
    public void testBasicUserLimitReached() {

        when(reservationService.getUserReservationCountOnDay(startDay, endDay, 1L)).thenReturn(1);

        assertThrows(InvalidReservationException.class, () -> {
            userReservationBalanceValidator.handle(reservation1);
        });
        verify(reservationService).getUserReservationCountOnDay(startDay, endDay, 1L);
    }

    @Test
    public void testPremiumUserLimitNotReachedYet() throws InvalidReservationException {
        reservation1.setMadeByPremiumUser(true);

        when(reservationService.getUserReservationCountOnDay(startDay, endDay, 1L)).thenReturn(2);

        this.userReservationBalanceValidator.handle(reservation1);

        verify(reservationService).getUserReservationCountOnDay(startDay, endDay, 1L);
    }

    @Test
    public void testPremiumUserLimitReached() {

        reservation1.setMadeByPremiumUser(true);
        when(reservationService.getUserReservationCountOnDay(startDay, endDay, 1L)).thenReturn(3);

        assertThrows(InvalidReservationException.class, () -> {
            userReservationBalanceValidator.handle(reservation1);
        });
        verify(reservationService).getUserReservationCountOnDay(startDay, endDay, 1L);

    }

}
