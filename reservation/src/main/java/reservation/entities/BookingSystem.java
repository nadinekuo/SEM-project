package reservation.entities;

import java.util.ArrayList;
import java.util.List;

public class BookingSystem {

    private transient ReservationSortingStrategy sortingStrategy;
    private transient List<Reservation> bookings;

    public BookingSystem(ReservationSortingStrategy sortingStrategy,
                         List<Reservation> reservationList) {
        this.sortingStrategy = sortingStrategy;
        this.bookings = new ArrayList<>();
    }

    public BookingSystem(ChronologicalStrategy sortingStrategy) {

    }

    public void addReservation(Reservation reservation) {
        bookings.add(reservation);
    }

    public Reservation getNextReservation() {
        return sortingStrategy.getNextReservation(bookings);
    }
}
