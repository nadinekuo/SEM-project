package reservation.chainofresponsibility;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.HttpClientErrorException;
import reservation.controllers.ReservationController;
import reservation.controllers.UserFacilityCommunicator;
import reservation.entities.Reservation;
import reservation.entities.ReservationType;
import reservation.entities.chainofresponsibility.InvalidReservationException;
import reservation.entities.chainofresponsibility.UserReservationBalanceValidator;
import reservation.services.ReservationService;

@RunWith(MockitoJUnitRunner.class)
public class UserReservationBalanceValidatorTest {

    private final transient Reservation reservation1;
    private final transient ReservationController reservationController;
    private final transient ReservationService reservationService;
    private final transient UserFacilityCommunicator userFacilityCommunicator;
    // Class under test:
    private final transient UserReservationBalanceValidator userReservationBalanceValidator;
    LocalDateTime startDay;
    LocalDateTime endDay;

    /**
     * Constructor for this test suite.
     */
    public UserReservationBalanceValidatorTest() {
        userFacilityCommunicator = mock(UserFacilityCommunicator.class);
        reservationService = mock(ReservationService.class);
        reservationController = mock(ReservationController.class);
        when(reservationController.getUserFacilityCommunicator())
            .thenReturn(userFacilityCommunicator);
        this.userReservationBalanceValidator =
            new UserReservationBalanceValidator(reservationService, reservationController);

        reservation1 = new Reservation(ReservationType.EQUIPMENT, "hockey", 1L, 42L,
            LocalDateTime.of(2022, 10, 05, 16, 00), false);
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
    public void constructorTest() {
        assertNotNull(userReservationBalanceValidator);
    }

    @Test
    public void basicUserLimitNotReachedYetTest() throws InvalidReservationException {
        when(userFacilityCommunicator.getUserExists(anyLong())).thenReturn(true);

        when(reservationService.getUserReservationCountOnDay(startDay, endDay, 1L)).thenReturn(0);

        this.userReservationBalanceValidator.handle(reservation1);

        verify(reservationService).getUserReservationCountOnDay(startDay, endDay, 1L);
    }

    @Test
    public void basicUserLimitReachedTest() {
        when(userFacilityCommunicator.getUserExists(anyLong())).thenReturn(true);

        when(reservationService.getUserReservationCountOnDay(startDay, endDay, 1L)).thenReturn(1);

        assertThrows(InvalidReservationException.class, () -> {
            userReservationBalanceValidator.handle(reservation1);
        });
        verify(reservationService).getUserReservationCountOnDay(startDay, endDay, 1L);
    }

    @Test
    public void premiumUserLimitNotReachedYetTest() throws InvalidReservationException {
        when(userFacilityCommunicator.getUserExists(anyLong())).thenReturn(true);
        reservation1.setMadeByPremiumUser(true);

        when(reservationService.getUserReservationCountOnDay(startDay, endDay, 1L)).thenReturn(2);

        this.userReservationBalanceValidator.handle(reservation1);

        verify(reservationService).getUserReservationCountOnDay(startDay, endDay, 1L);
    }

    @Test
    public void premiumUserLimitReachedTest() {
        when(userFacilityCommunicator.getUserExists(anyLong())).thenReturn(true);

        reservation1.setMadeByPremiumUser(true);
        when(reservationService.getUserReservationCountOnDay(startDay, endDay, 1L)).thenReturn(3);

        assertThrows(InvalidReservationException.class, () -> {
            userReservationBalanceValidator.handle(reservation1);
        });
        verify(reservationService).getUserReservationCountOnDay(startDay, endDay, 1L);

    }

    @Test
    public void userDoesNotExistTest() {
        when(userFacilityCommunicator.getUserExists(anyLong()))
            .thenThrow(HttpClientErrorException.class);
        when(reservationService.getUserReservationCountOnDay(any(), any(), any())).thenReturn(0);

        assertThrows(InvalidReservationException.class,
            () -> this.userReservationBalanceValidator.handle(reservation1));
    }

}
