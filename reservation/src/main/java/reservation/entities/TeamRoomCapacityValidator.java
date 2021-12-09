package reservation.entities;

public class TeamRoomCapacityValidator extends BaseValidator {

    @Override
    public boolean handle(Reservation reservation) throws InvalidReservationException {
        return false;
    }
}
