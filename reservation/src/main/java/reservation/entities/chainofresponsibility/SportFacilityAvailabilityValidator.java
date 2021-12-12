package reservation.entities.chainofresponsibility;

import com.netflix.discovery.converters.Auto;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import reservation.controllers.ReservationController;
import reservation.entities.Reservation;
import reservation.entities.ReservationType;
import reservation.repositories.ReservationRepository;
import reservation.services.ReservationService;

public class SportFacilityAvailabilityValidator extends BaseValidator {

    private final ReservationService reservationService;
    private final ReservationController reservationController;
    private final ReservationRepository reservationRepository;

    public SportFacilityAvailabilityValidator(ReservationService reservationService,
                                              ReservationController reservationController,
                                              ReservationRepository reservationRepository) {
        this.reservationService = reservationService;
        this.reservationController = reservationController;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public boolean handle(Reservation reservation) throws InvalidReservationException {

        boolean isGroupReservation = reservation.getGroupId() != -1;

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
            boolean sportsRoomAvailable = reservationService
                .sportsFacilityIsAvailable(sportsRoomId, reservation.getStartingTime());
            if (isGroupReservation) {
                if (reservationRepository.findByGroupIdandTime(reservation.getGroupId(),
                    reservation.getStartingTime()).isPresent()) {
                    sportsRoomAvailable = true;
                }
            }
            if (!sportsRoomAvailable) {
                throw new InvalidReservationException(
                    "Sports room is already booked for this time " + "slot: " + reservation
                        .getStartingTime());
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
