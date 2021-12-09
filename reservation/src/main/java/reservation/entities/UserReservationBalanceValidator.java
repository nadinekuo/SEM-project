package reservation.entities;


public class UserReservationBalanceValidator extends BaseValidator {

    @Override
    public boolean handle(Reservation reservation) throws InvalidReservationException {
        return false;
    }
}
