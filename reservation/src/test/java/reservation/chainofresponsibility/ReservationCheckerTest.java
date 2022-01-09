package reservation.chainofresponsibility;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

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
import reservation.entities.chainofresponsibility.SportFacilityAvailabilityValidator;
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

        doNothing().when(userReservationBalanceValidator).handle(any());

        assertDoesNotThrow(() -> reservationCheckerSpy.checkReservation(new Reservation(),
            new ReservationController(reservationService, reservationCheckerSpy)));
    }

    @Test
    void checkInvalidReservationTest() throws InvalidReservationException {

        ReservationChecker reservationCheckerSpy =
            Mockito.spy(new ReservationChecker(reservationService));
        Mockito.doReturn(userReservationBalanceValidator).when(reservationCheckerSpy)
            .createChainOfResponsibility(any(), any());

        doThrow(InvalidReservationException.class).when(userReservationBalanceValidator)
            .handle(any());

        assertThrows(InvalidReservationException.class,
            () -> reservationCheckerSpy.checkReservation(new Reservation(),
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
            new SportFacilityAvailabilityValidator(reservationService, reservationController);
        userBalanceHandler.setNext(sportFacilityHandler);
        assertThat(reservationChecker.createChainOfResponsibility(new Reservation(),
            reservationController)).usingRecursiveComparison().isEqualTo(userBalanceHandler);
    }

    @Test
    void createChainOfResponsibilitySportRoom() {
        ReservationChecker reservationChecker = new ReservationChecker(reservationService);
        ReservationController reservationController =
            new ReservationController(reservationService, reservationChecker);
        ReservationValidator userBalanceHandler =
            new UserReservationBalanceValidator(reservationService, reservationController);
        ReservationValidator sportFacilityHandler =
            new SportFacilityAvailabilityValidator(reservationService, reservationController);
        userBalanceHandler.setNext(sportFacilityHandler);
        ReservationValidator capacityHandler =
            new TeamRoomCapacityValidator(reservationController);
        sportFacilityHandler.setNext(capacityHandler);
        Reservation reservation = new Reservation();
        reservation.setTypeOfReservation(ReservationType.SPORTS_ROOM);
        assertThat(reservationChecker.createChainOfResponsibility(reservation,
            reservationController)).usingRecursiveComparison().isEqualTo(userBalanceHandler);
    }

}