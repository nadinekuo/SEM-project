package reservation.entities;

import java.util.List;

public interface ReservationSortingStrategy {

    Reservation getNextReservation(List<Reservation> reservations);
}
