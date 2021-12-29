package reservation.entities.chainofresponsibility;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reservation.controllers.ReservationController;
import reservation.entities.Reservation;
import reservation.services.ReservationService;

@Component
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
    @Autowired
    public UserReservationBalanceValidator(ReservationService reservationService,
                                           ReservationController reservationController) {
        this.reservationService = reservationService;
        this.reservationController = reservationController;
    }

    @Override
    public boolean handle(Reservation reservation) throws InvalidReservationException {

        // We want to count all reservations from 00:00 to 23:59 on the day specified.
        LocalDateTime startingTimeReservation = reservation.getStartingTime();
        LocalDateTime startDay = getStartOfDay(startingTimeReservation);
        LocalDateTime endDay = getEndOfDay(startingTimeReservation);

        int reservationBalanceOnDate = reservationService
            .getUserReservationCountOnDay(startDay, endDay, reservation.getCustomerId());

        // Communicates with user microservice
        boolean isPremium = reservationController.getUserIsPremium(reservation.getCustomerId());

//        System.out.println(
//            "########## USER ID " + reservation.getCustomerId() + " (Premium: " + isPremium + ") "
//                + " HAS " + reservationBalanceOnDate + " RESERVATIONS FOR THE DATE " + reservation
//                .getStartingTime().toString());

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



    /**
     * @param startingTime  starting time of Reservation object to be checked
     * @return LocalDateTime of same day, but time 00:00:00, which is the start of the day.
     */
    private LocalDateTime getEndOfDay(LocalDateTime startingTime) {
        return LocalDateTime.parse(startingTime.toString().substring(0, 10) + "T00:00:00");
    }

    /**
     * @param startingTime - starting time of Reservation object to be checked
     * @return LocalDateTime of same day, but time 23:59:59, which is the end of the day.
     */
    private LocalDateTime getStartOfDay(LocalDateTime startingTime) {
        return LocalDateTime.parse(startingTime.toString().substring(0, 10) + "T23:59:59");
    }



}
