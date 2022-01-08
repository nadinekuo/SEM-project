package reservation.entities.chainofresponsibility;

import reservation.entities.Reservation;

public interface ReservationValidator {


    void setNext(ReservationValidator handler);

    void handle(Reservation reservation) throws InvalidReservationException;


}
