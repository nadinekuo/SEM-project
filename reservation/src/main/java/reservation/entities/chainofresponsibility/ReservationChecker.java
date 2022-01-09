package reservation.entities.chainofresponsibility;

import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import reservation.controllers.ReservationController;
import reservation.entities.Reservation;
import reservation.entities.ReservationType;
import reservation.services.ReservationService;

@Component("reservationChecker")
public class ReservationChecker {

    private final ReservationService reservationService;

    /**
     * Constructor.
     *
     * @param reservationService reservation service
     */
    public ReservationChecker(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * Check reservation by passing the object through Chain of Responsibility.
     * Various checks to be done by different validators.
     * If the reservation was not valid, that means one or more checks (in some validator)
     * were violated -> exception thrown.
     *
     * @param reservation           the reservation
     * @param reservationController the reservation controller through which API calls to other
     *                              microservices are made
     * @return boolean - true if Reservation can be made, else false.
     */
    public void checkReservation(Reservation reservation,
                                    ReservationController reservationController)
        throws InvalidReservationException {

        // Returns first validator in chain created for this reservation
        ReservationValidator reservationValidator =
            createChainOfResponsibility(reservation, reservationController);

        try {
            reservationValidator.handle(reservation);   // Start of chain
        } catch (InvalidReservationException | HttpClientErrorException e) {
            throw new InvalidReservationException(e.getMessage());
        }
    }

    /**
     * Creates Chain of Responsibility object.
     * Having a separate method for this creation, facilitates testability.
     *
     * @param reservation           - Reservation to be checked
     * @param controller - API to communicate with other microservices
     * @return - The first validator in the chain of responsibility created
     */
    public ReservationValidator createChainOfResponsibility(Reservation reservation,
                                                            ReservationController controller) {

        // Checks whether or not customers have exceeded their daily
        // reservation limit for sport rooms
        ReservationValidator userBalanceHandler =
            new UserReservationBalanceValidator(reservationService, controller);

        // Checks whether the reserved sports room or equipment is available for reservation
        ReservationValidator sportFacilityHandler =
            new SportFacilityAvailabilityValidator(reservationService, controller);
        userBalanceHandler.setNext(sportFacilityHandler);

        // Only for sports room reservations, we check the room capacity/team size
        if (reservation.getTypeOfReservation() == ReservationType.SPORTS_ROOM) {

            //  Checks the compatibility of the reserved sports room (hall/field) capacity
            //  with the group size of the customers who want to reserve that sports room
            // For sport fields (hold 1 sport),
            // the team size requirements of that sport is also checked
            ReservationValidator capacityHandler =
                new TeamRoomCapacityValidator(controller);
            sportFacilityHandler.setNext(capacityHandler);
        }
        return userBalanceHandler;
    }
}