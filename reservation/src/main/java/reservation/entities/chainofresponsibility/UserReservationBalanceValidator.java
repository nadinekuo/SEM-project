package reservation.entities.chainofresponsibility;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import reservation.controllers.ReservationController;
import reservation.controllers.UserFacilityCommunicator;
import reservation.entities.Reservation;
import reservation.services.ReservationService;

@Component
public class UserReservationBalanceValidator extends BaseValidator {

    private final ReservationService reservationService;
    private final ReservationController reservationController;
    private final UserFacilityCommunicator userFacilityCommunicator;



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
        this.userFacilityCommunicator = this.reservationController.getUserFacilityCommunicator();
    }

    @Override
    public void handle(Reservation reservation) throws InvalidReservationException {

        // We want to count all reservations from 00:00 to 23:59 on the day specified.
        LocalDateTime startingTimeReservation = reservation.getStartingTime();
        LocalDateTime startDay = getStartOfDay(startingTimeReservation);
        LocalDateTime endDay = getEndOfDay(startingTimeReservation);

        int reservationBalanceOnDate = reservationService
            .getUserReservationCountOnDay(startDay, endDay, reservation.getCustomerId());

        try {
            userFacilityCommunicator.getUserExists(reservation.getCustomerId());
        } catch (HttpClientErrorException e) {
            throw new InvalidReservationException(
                "user with id " + reservation.getCustomerId() + " does not exist");
        }

        boolean isPremium = reservation.getMadeByPremiumUser();

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

        super.checkNext(reservation);
    }



    /**
     * Gets the first second of a certain day.
     *
     * @param startingTime starting time of Reservation object to be checked
     * @return LocalDateTime of same day, but time 00:00:00, which is the start of the day.
     */
    private LocalDateTime getStartOfDay(LocalDateTime startingTime) {
        return LocalDateTime.parse(startingTime.toString().substring(0, 10) + "T00:00:00");
    }

    /**
     * Gets the last second of the day.
     *
     * @param startingTime - starting time of Reservation object to be checked
     * @return LocalDateTime of same day, but time 23:59:59, which is the end of the day.
     */
    private LocalDateTime getEndOfDay(LocalDateTime startingTime) {
        return LocalDateTime.parse(startingTime.toString().substring(0, 10) + "T23:59:59");
    }



}
