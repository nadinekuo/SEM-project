package reservationPackage.entities;

import java.util.ArrayList;
import java.util.List;

public interface ReservationSortingStrategy {

    public Reservation getNextReservation(List<Reservation> reservations);
}
