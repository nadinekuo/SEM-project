package reservation.entities.chainofresponsibility;

import java.time.LocalDateTime;
import reservation.entities.Reservation;
import reservation.entities.chainofresponsibility.BaseValidator;
import reservation.entities.chainofresponsibility.InvalidReservationException;
import reservation.services.ReservationService;

public class UserReservationBalanceValidator extends BaseValidator {

    private ReservationService reservationService;
    private boolean isPremium;


    public UserReservationBalanceValidator(ReservationService reservationService,
                                           boolean isPremium) {
        this.reservationService = reservationService;
        this.isPremium = isPremium;
    }

    @Override
    public boolean handle(Reservation reservation) throws InvalidReservationException {

        LocalDateTime startDay = LocalDateTime.parse(reservation.getStartingTime()
            .toString().substring(0, 10) + "T00:00:00");
        LocalDateTime endDay = LocalDateTime.parse(reservation.getStartingTime()
            .toString().substring(0, 10) + "T23:59:59");

        int reservationBalanceOnDate =
            reservationService.getUserReservationCountOnDay(startDay, endDay, reservation.getCustomerId());

        // Basic users can have 1 sports room reservation per day

        if (!isPremium && reservationBalanceOnDate == 1) {
            throw new InvalidReservationException("Daily limit of 1 reservation per day has "
                + "been "
                + "reached!");
        }

        // Premium users can have up to 3 reservations per day
        if (isPremium && reservationBalanceOnDate == 3) {
            throw new InvalidReservationException("Daily limit of 3 reservations per day has been "
                + "reached!");
        }

        return super.checkNext(reservation);
    }
}
