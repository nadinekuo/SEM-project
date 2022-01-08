package reservation.entities.chainofresponsibility;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reservation.controllers.ReservationController;
import reservation.controllers.SportFacilityCommunicator;
import reservation.controllers.UserFacilityCommunicator;
import reservation.entities.Reservation;
import reservation.services.ReservationService;

@Component
public class TeamRoomCapacityValidator extends BaseValidator {

    //TODO make use of this variable or remove it
    private final ReservationService reservationService;
    private final ReservationController reservationController;
    private final SportFacilityCommunicator sportFacilityCommunicator;
    private final UserFacilityCommunicator userFacilityCommunicator;


    /**
     * Instantiates a new Team / Room capacity validator.
     * Checks:
     * - whether the min/max capacity of the sport room is adhered to
     * for group reservations only:
     * - whether group size adheres to min/max constraints on team sport teams
     * Note: this validator is only part of the chain for sport room reservations
     * (not equipment reservations)
     *
     * @param reservationService    -  the reservation service containing logic
     * @param reservationController the reservation controller to communicate with other
     *                              microservices
     */
    @Autowired
    public TeamRoomCapacityValidator(ReservationService reservationService,
                                     ReservationController reservationController) {
        this.reservationService = reservationService;
        this.reservationController = reservationController;
        this.sportFacilityCommunicator = this.reservationController.getSportFacilityCommunicator();
        this.userFacilityCommunicator = this.reservationController.getUserFacilityCommunicator();
    }

    @Override
    public boolean handle(Reservation reservation) throws InvalidReservationException {

        long roomId = reservation.getSportFacilityReservedId();

        // We know that there exists an existing sports room for this id,
        // since that was checked by the SportFacilityAvailabilityValidator

        // Halls can hold multiple (team) sports, whereas fields are tied to 1 team sport.
        boolean isSportHall = sportFacilityCommunicator.getIsSportHall(roomId);

        boolean isGroupReservation = (reservation.getGroupId() != -1);
        int groupSize;

        // Make request to user service controller to get group size
        if (isGroupReservation) {
            groupSize = userFacilityCommunicator.getGroupSize(reservation.getGroupId());
        } else {
            groupSize = 1;
        }

        // Only for sport fields (hold 1 specific team sport),
        // we check whether group size adheres to the sport's min and max team size constraints.
        if (!isSportHall) {

            String sportName = sportFacilityCommunicator.getSportFieldSport(roomId);
            int minTeamSize = sportFacilityCommunicator.getSportMinTeamSize(sportName);
            int maxTeamSize = sportFacilityCommunicator.getSportMaxTeamSize(sportName);

            if (groupSize < minTeamSize || groupSize > maxTeamSize) {
                throw new InvalidReservationException(
                    "Group size for a " + sportName + " team " + "should be between " + minTeamSize
                        + " and " + maxTeamSize + "!" + "\n\n Your group (ID "
                        + reservation.getGroupId() + " has size: " + groupSize);
            }

        }

        int roomMinCapacity = sportFacilityCommunicator.getSportRoomMinimumCapacity(roomId);
        int roomMaxCapacity = sportFacilityCommunicator.getSportRoomMaximumCapacity(roomId);

        // A single user/group has full access to the reserved sports room, meaning no other
        // customers can enter that room during that time slot.
        // Thus, we can just check the group size against room capacity.

        if (groupSize < roomMinCapacity || groupSize > roomMaxCapacity) {
            throw new InvalidReservationException(
                "This sports room has a minimal capacity of " + roomMinCapacity
                    + " and a maximal capacity of " + roomMaxCapacity + "!"
                    + "\n\n Your group size: " + groupSize);
        }

        return super.checkNext(reservation);
    }
}
