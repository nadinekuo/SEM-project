package nl.tudelft.sem.reservation.entities.chainofresponsibility;

import nl.tudelft.sem.reservation.entities.Reservation;

public interface ReservationValidator {


    void setNext(ReservationValidator handler);

    void handle(Reservation reservation) throws InvalidReservationException;


}
