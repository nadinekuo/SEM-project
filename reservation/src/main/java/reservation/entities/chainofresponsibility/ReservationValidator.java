package reservation.entities.chainofresponsibility;

import reservation.entities.Reservation;

public interface ReservationValidator {


    void setNext(ReservationValidator handler);

    boolean handle(Reservation reservation) throws InvalidReservationException;


}
