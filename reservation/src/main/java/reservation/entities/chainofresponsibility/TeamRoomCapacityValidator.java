package reservation.entities.chainofresponsibility;

import reservation.controllers.ReservationController;
import reservation.entities.Reservation;
import reservation.entities.chainofresponsibility.BaseValidator;
import reservation.entities.chainofresponsibility.InvalidReservationException;
import reservation.services.ReservationService;

public class TeamRoomCapacityValidator extends BaseValidator {


    private ReservationService reservationService;
    private ReservationController reservationController;

    public TeamRoomCapacityValidator(ReservationService reservationService,
                                     ReservationController reservationController) {
        this.reservationService = reservationService;
        this.reservationController = reservationController;
    }


    @Override
    public boolean handle(Reservation reservation) throws InvalidReservationException {

        long roomId = reservation.getSportFacilityReservedId();

        // We know that there exists an existing sports room for this id,
        // since that was checked by the SportFacilityAvailabilityValidator

        boolean isSportHall =
            reservationController.getIsSportHall(roomId);

        boolean isGroupReservation = reservation.getGroupId() != -1;
        int groupSize;

        // Make request to user service controller
        if (isGroupReservation) {
            groupSize = reservationController.getGroupSize(reservation.getGroupId());
        } else {
            groupSize = 1;
        }

        // Only for sport fields (hold 1 specific sport),
        // we check whether team size adheres to the sport's min and max team size constraints.
        if (!isSportHall) {

            String sportName = reservationController.getSportFieldSport(roomId);
            int minTeamSize = reservationController.getSportMinTeamSize(sportName);
            int maxTeamSize = reservationController.getSportMaxTeamSize(sportName);

            if (groupSize < minTeamSize || groupSize > maxTeamSize) {
                throw new InvalidReservationException("Group size for a " + sportName + " team "
                    + "should be between " + minTeamSize + " and " + maxTeamSize + "!");
            }

        }

        // Each team member has a separate reservation, which is checked individually.

        int roomMinCapacity = reservationController.getSportRoomMinimumCapacity(roomId);
        int roomMaxCapacity = reservationController.getSportRoomMaximumCapacity(roomId);

        // A single user/group has full access to the reserved sports room, meaning no other
        // customers can enter that room during that time slot.
        // Thus, we can just check the group size.

        if (groupSize < roomMinCapacity || groupSize > roomMaxCapacity) {
            throw new InvalidReservationException("This sports room has a minimal capacity of "
                + roomMinCapacity + " and a maximal capacity of " + roomMaxCapacity + "!");
        }

        return super.checkNext(reservation);
    }
}
