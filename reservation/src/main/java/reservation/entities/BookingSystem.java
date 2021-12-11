package reservation.entities;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.client.RestTemplate;

public class BookingSystem {

    private transient ReservationSortingStrategy sortingStrategy;
    private transient List<Reservation> bookings;

    public BookingSystem(ReservationSortingStrategy sortingStrategy) {
        this.sortingStrategy = sortingStrategy;
        this.bookings = new ArrayList<>();
    }

    public void addReservation(Reservation reservation) {
        bookings.add(reservation);
    }

    public Reservation getNextReservation() {
        return sortingStrategy.getNextReservation(bookings);
    }

    @Override
    public String toString() {
        return "BookingSystem{" + "bookings=" + bookings + '}';
    }
}
