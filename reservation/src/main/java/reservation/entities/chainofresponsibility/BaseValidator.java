package reservation.entities.chainofresponsibility;

import reservation.entities.Reservation;


public abstract class BaseValidator implements ReservationValidator {

    private ReservationValidator next;

    public void setNext(ReservationValidator nextValidator) {
        this.next = nextValidator;
    }


    protected boolean checkNext(Reservation reservation) throws InvalidReservationException {

        if (next == null) {
            return true;
        }

        // Handling the next element in the chain
        return next.handle(reservation);
    }



}
