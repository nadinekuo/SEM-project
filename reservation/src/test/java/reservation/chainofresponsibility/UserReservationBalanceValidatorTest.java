package reservation.chainofresponsibility;

import java.time.LocalDateTime;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reservation.controllers.ReservationController;
import reservation.entities.Reservation;
import reservation.entities.ReservationType;
import reservation.entities.chainofresponsibility.UserReservationBalanceValidator;
import reservation.services.ReservationService;

@ExtendWith(MockitoExtension.class)
public class UserReservationBalanceValidatorTest {

    @Mock
    private transient ReservationController reservationController;
    @Mock
    private transient ReservationService reservationService;

    private transient UserReservationBalanceValidator userReservationBalanceValidator;

    private transient Reservation reservation1;
    private transient Reservation reservation2;
    private transient Reservation groupReservation1;

    @BeforeEach
    void setUp() {
        //        reservationService = mock(ReservationService.class);
        //        reservationController = mock(ReservationController.class);
        userReservationBalanceValidator =
            new UserReservationBalanceValidator(reservationService, reservationController);

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

    @Test
    public void testLocalDateStringParsing() {

    }

}
