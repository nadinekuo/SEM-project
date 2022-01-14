package nl.tudelft.sem.reservation.entities.chainofresponsibility;

import nl.tudelft.sem.reservation.entities.Reservation;
import org.springframework.web.client.HttpClientErrorException;

public abstract class BaseValidator implements ReservationValidator {

    private ReservationValidator next;

    public void setNext(ReservationValidator nextValidator) {
        this.next = nextValidator;
    }

    protected void checkNext(Reservation reservation) throws InvalidReservationException {

        if (next == null) {
            return;
        }

        // Handling the next element in the chain
        try {
            next.handle(reservation);
        } catch (InvalidReservationException | HttpClientErrorException e) {
            throw new InvalidReservationException(e.getMessage());
        }

    }

}
