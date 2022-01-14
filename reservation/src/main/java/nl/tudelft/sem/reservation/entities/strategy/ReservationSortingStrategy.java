package nl.tudelft.sem.reservation.entities.strategy;

import java.util.List;
import nl.tudelft.sem.reservation.entities.Reservation;

public interface ReservationSortingStrategy {

    Reservation getNextReservation(List<Reservation> reservations);
}
