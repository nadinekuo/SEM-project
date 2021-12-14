package reservation.entities.chainofresponsibility;

import java.time.LocalDateTime;
import reservation.controllers.ReservationController;
import reservation.entities.Reservation;
import reservation.entities.ReservationType;
import reservation.services.ReservationService;

/**
 * The type Sport facility availability validator.
 */
public class SportFacilityAvailabilityValidator extends BaseValidator {

    private final ReservationService reservationService;
    private final ReservationController reservationController;

    /**
     * Instantiates a new Sport facility availability validator.
     *
     * @param reservationService    the reservation service
     * @param reservationController the reservation controller
     */
    public SportFacilityAvailabilityValidator(ReservationService reservationService,
                                              ReservationController reservationController) {
        this.reservationService = reservationService;
        this.reservationController = reservationController;
    }

    @Override
    public boolean handle(Reservation reservation) throws InvalidReservationException {

        if (reservation.getStartingTime().isBefore(LocalDateTime.now())) {
            throw new InvalidReservationException("Invalid starting time of reservation!");
        }

        // Only between 16:00 and 23:00 reservations can be made
        if ((reservation.getStartingTime().getHour() < 16) || (
            reservation.getStartingTime().getHour() == 23)) {
            throw new InvalidReservationException(
                "Reservation slot has to be between 16:00 and " + "23:00.");
        }

        if (reservation.getTypeOfReservation() == ReservationType.SPORTS_FACILITY) {

            long sportsRoomId = reservation.getSportFacilityReservedId();

            // Check if sport room is not reserved already for this time slot (false)
            // If true, it may not necessarily exist.

            boolean isGroupReservation = reservation.getGroupId() != -1;

            boolean sportsRoomAvailable;

            if (isGroupReservation &&
                reservationService.findByGroupIdAndTime(reservation.getGroupId(),
                    reservation.getStartingTime()) != null) {
                sportsRoomAvailable = true;
            } else {
                sportsRoomAvailable = reservationService.sportsFacilityIsAvailable(sportsRoomId,
                    reservation.getStartingTime());
            }
            if (!sportsRoomAvailable) {
                throw new InvalidReservationException(
                    "Sports room is already booked for this time " + "slot: "
                        + reservation.getStartingTime());
            }

            // Call Sports Facilities service: check if sports room exists
            boolean sportsRoomExists = reservationController.getSportsRoomExists(sportsRoomId);
            if (!sportsRoomExists) {
                throw new InvalidReservationException("Sports room does not exist.");
            }

        } else if (reservation.getTypeOfReservation() == ReservationType.EQUIPMENT) {

            // Check if equipment existing / available instance found
            if (reservation.getSportFacilityReservedId() == -1L) {
                throw new InvalidReservationException("Equipment name invalid or not in stock!");
            }
        }

        return super.checkNext(reservation);
    }
}
