package reservation.entities.chainofresponsibility;

import java.time.LocalDateTime;
import reservation.controllers.ReservationController;
import reservation.entities.Reservation;
import reservation.services.ReservationService;

public class UserReservationBalanceValidator extends BaseValidator {

    private final ReservationService reservationService;
    private final ReservationController reservationController;

    /**
     * Instantiates a new User Reservation Balance validator.
     *  Checks:
     *  - whether daily limit on sports room reservations is not reached yet
     *
     * @param reservationService  -  the reservation service containing logic
     * @param reservationController the reservation controller to communicate with other
     *                              microservices
     */
    public UserReservationBalanceValidator(ReservationService reservationService,
                                           ReservationController reservationController) {
        this.reservationService = reservationService;
        this.reservationController = reservationController;
    }

    @Override
    public boolean handle(Reservation reservation) throws InvalidReservationException {

        // We want to count all reservations from 00:00 to 23:59 on the day specified.
        LocalDateTime startDay = LocalDateTime
            .parse(reservation.getStartingTime().toString().substring(0, 10) + "T00:00:00");
        LocalDateTime endDay = LocalDateTime
            .parse(reservation.getStartingTime().toString().substring(0, 10) + "T23:59:59");

        int reservationBalanceOnDate = reservationService
            .getUserReservationCountOnDay(startDay, endDay, reservation.getCustomerId());

        // Communicates with user microservice
        boolean isPremium = reservationController.getUserIsPremium(reservation.getCustomerId());

        System.out.println(
            "########## USER ID " + reservation.getCustomerId() + " (Premium: " + isPremium + ") "
                + " HAS " + reservationBalanceOnDate + " RESERVATIONS FOR THE DATE " + reservation
                .getStartingTime().toString());

        // Basic users can have 1 sports room reservation per day
        if (!isPremium && reservationBalanceOnDate >= 1) {
            throw new InvalidReservationException(
                "Daily limit of 1 reservation per day has " + "been reached!");
        }

        // Premium users can have up to 3 reservations per day
        if (isPremium && reservationBalanceOnDate >= 3) {
            throw new InvalidReservationException(
                "Daily limit of 3 reservations per day has been " + "reached!");
        }

        return super.checkNext(reservation);
    }
}
