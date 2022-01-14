package nl.tudelft.sem.reservation.entities.chainofresponsibility;

import java.time.LocalDateTime;
import nl.tudelft.sem.reservation.controllers.ReservationController;
import nl.tudelft.sem.reservation.controllers.SportFacilityCommunicator;
import nl.tudelft.sem.reservation.entities.Reservation;
import nl.tudelft.sem.reservation.entities.ReservationType;
import nl.tudelft.sem.reservation.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SportFacilityAvailabilityValidator extends BaseValidator {

    private final ReservationService reservationService;
    private final ReservationController reservationController;
    private final SportFacilityCommunicator sportFacilityCommunicator;

    /**
     * Instantiates a new Sport facility availability validator.
     * Checks:
     * - whether starting time of reservation is between 16:00 and 23:00
     * - whether sport room or equipment is physically available
     *
     * @param reservationService    -  the reservation service containing logic
     * @param reservationController the reservation controller to communicate with other
     *                              microservices
     */
    @Autowired
    public SportFacilityAvailabilityValidator(ReservationService reservationService,
                                              ReservationController reservationController) {
        this.reservationService = reservationService;
        this.reservationController = reservationController;
        this.sportFacilityCommunicator = this.reservationController.getSportFacilityCommunicator();
        //this.sportFacilityCommunicator = sportFacilityCommunicator;
    }

    @Override
    public void handle(Reservation reservation) throws InvalidReservationException {

        checkTime(reservation);

        if (reservation.getTypeOfReservation() == ReservationType.SPORTS_ROOM) {
            checkExistingAvailableSportsRoom(reservation);

        } else if (reservation.getTypeOfReservation() == ReservationType.EQUIPMENT) {

            // Check if equipment existing / available instance found
            // Note: the Sports facility microservice was called before this Reservation object
            // was created, in ReservationController
            // If no instance was found, we set it to -1L there, which is now being checked.
            if (reservation.getSportFacilityReservedId() == -1L) {
                throw new InvalidReservationException("Equipment name invalid or not in stock!");
            }
        }
        super.checkNext(reservation);
    }

    /**
     * Checks whether starting time of reservation is valid.
     *
     * @param reservation - reservation object to be checked
     * @throws InvalidReservationException - message contains error
     */
    private void checkTime(Reservation reservation) throws InvalidReservationException {

        if (reservation.getStartingTime().isBefore(LocalDateTime.now())) {
            throw new InvalidReservationException("Invalid starting time of reservation!");
        }

        // Only between 16:00 and 23:00 reservations can be made
        if ((reservation.getStartingTime().getHour() < 16) || (
            reservation.getStartingTime().getHour() == 23)) {
            throw new InvalidReservationException(
                "Reservation slot has to be between 16:00 and " + "23:00.");
        }
    }

    /**
     * Checks whether sport room id of reservation object belongs to an available, existing room.
     *
     * @param reservation - reservation object to be checked
     * @throws InvalidReservationException - message contains error
     */
    private void checkExistingAvailableSportsRoom(Reservation reservation)
        throws InvalidReservationException {

        long sportsRoomId = reservation.getSportFacilityReservedId();

        // Check if sport room is not reserved already for this time slot (false)

        // For group reservations, all members have an individual reservation for the same room,
        // but that should not make that room unavailable, so we add a flag.
        boolean isGroupReservation = (reservation.getGroupId() != -1);

        boolean sportsRoomAvailable;
        if (isGroupReservation && reservationService.findByGroupIdAndTime(reservation.getGroupId(),
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
        // Even if the room was "available", it may not necessarily exist!
        boolean sportsRoomExists = sportFacilityCommunicator.getSportsRoomExists(sportsRoomId);
        if (!sportsRoomExists) {
            throw new InvalidReservationException("Sports room does not exist.");
        }
    }

}
