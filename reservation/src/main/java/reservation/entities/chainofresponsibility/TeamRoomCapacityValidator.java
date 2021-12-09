package reservation.entities.chainofresponsibility;

import reservation.entities.Reservation;
import reservation.entities.chainofresponsibility.BaseValidator;
import reservation.entities.chainofresponsibility.InvalidReservationException;

public class TeamRoomCapacityValidator extends BaseValidator {

    @Override
    public boolean handle(Reservation reservation) throws InvalidReservationException {
        return false;
    }
}
