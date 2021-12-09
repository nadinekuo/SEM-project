package reservation.entities;

public class SportFacilityAvailabilityValidator extends BaseValidator {

    @Override
    public boolean handle(Reservation reservation) throws InvalidReservationException {
        return false;
    }
}
