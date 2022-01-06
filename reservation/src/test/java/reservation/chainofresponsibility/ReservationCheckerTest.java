package reservation.chainofresponsibility;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reservation.controllers.ReservationController;
import reservation.entities.Reservation;
import reservation.entities.ReservationType;
import reservation.entities.chainofresponsibility.InvalidReservationException;
import reservation.entities.chainofresponsibility.ReservationChecker;
import reservation.entities.chainofresponsibility.ReservationValidator;
import reservation.entities.chainofresponsibility.TeamRoomCapacityValidator;
import reservation.entities.chainofresponsibility.UserReservationBalanceValidator;
import reservation.services.ReservationService;

@ExtendWith(MockitoExtension.class)
class ReservationCheckerTest {

    @Mock
    private transient UserReservationBalanceValidator userReservationBalanceValidator;

    @Mock
    private transient ReservationService reservationService;

    @Test
    void checkValidReservationTest() throws InvalidReservationException {

        ReservationChecker reservationCheckerSpy =
            Mockito.spy(new ReservationChecker(reservationService));
        Mockito.doReturn(userReservationBalanceValidator).when(reservationCheckerSpy)
            .createChainOfResponsibility(any(), any());

        when(userReservationBalanceValidator.handle(any())).thenReturn(true);

        assertTrue(reservationCheckerSpy.checkReservation(new Reservation(),
            new ReservationController(reservationService, reservationCheckerSpy)));
    }

    @Test
    void checkInvalidReservationTest() throws InvalidReservationException {

        ReservationChecker reservationCheckerSpy =
            Mockito.spy(new ReservationChecker(reservationService));
        Mockito.doReturn(userReservationBalanceValidator).when(reservationCheckerSpy)
            .createChainOfResponsibility(any(), any());

        when(userReservationBalanceValidator.handle(any())).thenReturn(false);

        assertFalse(reservationCheckerSpy.checkReservation(new Reservation(),
            new ReservationController(reservationService, reservationCheckerSpy)));
    }

    @Test
    void createChainOfResponsibilityNotSportRoom() {
        ReservationChecker reservationChecker = new ReservationChecker(reservationService);
        ReservationController reservationController =
            new ReservationController(reservationService, reservationChecker);
        ReservationValidator userBalanceHandler =
            new UserReservationBalanceValidator(reservationService, reservationController);

        ReservationValidator sportFacilityHandler =
            new UserReservationBalanceValidator(reservationService, reservationController);
        userBalanceHandler.setNext(sportFacilityHandler);
        assertThat(reservationChecker
            .createChainOfResponsibility(new Reservation(), reservationController))
            .usingRecursiveComparison().isEqualTo(userBalanceHandler);
    }

    @Test
    void createChainOfResponsibilitySportRoom() {
        ReservationChecker reservationChecker = new ReservationChecker(reservationService);
        ReservationController reservationController =
            new ReservationController(reservationService, reservationChecker);
        ReservationValidator userBalanceHandler =
            new UserReservationBalanceValidator(reservationService, reservationController);
        ReservationValidator sportFacilityHandler =
            new UserReservationBalanceValidator(reservationService, reservationController);
        userBalanceHandler.setNext(sportFacilityHandler);
        ReservationValidator capacityHandler =
            new TeamRoomCapacityValidator(reservationService, reservationController);
        sportFacilityHandler.setNext(capacityHandler);
        Reservation reservation = new Reservation();
        reservation.setTypeOfReservation(ReservationType.SPORTS_ROOM);
        assertThat(
            reservationChecker.createChainOfResponsibility(reservation, reservationController))
            .usingRecursiveComparison().isEqualTo(userBalanceHandler);
    }

}