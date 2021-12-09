package reservation.entities.chainofresponsibility;

import reservation.entities.Reservation;
import reservation.entities.chainofresponsibility.BaseValidator;
import reservation.entities.chainofresponsibility.InvalidReservationException;

public class TeamRoomCapacityValidator extends BaseValidator {

    @Override
    public boolean handle(Reservation reservation) throws InvalidReservationException {

        // Todo: if isSportHall == false --> check team size for team Sport

        // Todo: check min/max capacity of sport room (hall/field)

        return super.checkNext(reservation);
    }
}
