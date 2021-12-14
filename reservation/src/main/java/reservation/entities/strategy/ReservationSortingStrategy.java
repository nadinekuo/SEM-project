package reservation.entities.strategy;

import java.util.List;
import reservation.entities.Reservation;

public interface ReservationSortingStrategy {

    Reservation getNextReservation(List<Reservation> reservations);
}
